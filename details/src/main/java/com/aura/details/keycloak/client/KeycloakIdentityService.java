package com.aura.details.keycloak.client;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.exceptions.UnexpectedTechnicalException;
import com.aura.core.exceptions.UserIdentityAlreadyExistsException;
import com.aura.core.exceptions.UserIdentityNotCreatedException;
import com.aura.details.keycloak.client.exceptions.UserAlreadyTaggedToTenantException;
import com.aura.details.keycloak.client.exceptions.UserIdentityNotDeletedException;
import com.aura.details.keycloak.client.mappers.UserIdentityMapper;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;


import static com.aura.core.clients.UserIdentityAttributeKeys.TENANT_ID_ATTR_KEY;

@Slf4j
@RequiredArgsConstructor
public class KeycloakIdentityService implements IdentityService {
    private final ClientResource clientResource;
    private final UsersResource usersResource;
    private final int actionTokenLifespanSeconds;
    /**
     * Keycloak client_id (OIDC client name) the action-email link redirects
     * through. Required by Keycloak whenever a non-null redirectUri is supplied
     * — Keycloak validates the URI against this client's Valid Redirect URIs.
     * Reuses the admin client by default; can be overridden via config if the
     * end-user-facing client (e.g. the web app) differs from the backend
     * service-account client.
     */
    private final String actionEmailClientId;

    @Override
    public List<UserIdentity> findByEmail(String email) {
        List<UserRepresentation> userRepresentations = usersResource.searchByEmail(email, true);
        return userRepresentations.stream().map(UserIdentityMapper.INSTANCE::toUserIdentity).toList();
    }

    /**
     * throws UserIdentityAlreadyExistsException, UserIdentityNotCreatedException
     *
     * @param userIdentity
     * @param tenantId
     * @return
     */
    @Override
    public UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId) {

        Map<String, List<String>> extAttributes = Map.of(TENANT_ID_ATTR_KEY, List.of(tenantId));
        userIdentity.setAttributes(extAttributes);
        UserRepresentation userRepresentation = UserIdentityMapper.INSTANCE.toUserRepresentation(userIdentity);
        userRepresentation.setAttributes(extAttributes);
        Response response = usersResource.create(userRepresentation);
        if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            String message = String.format("Error creating user identity: %s, with status code %s and reason %s", userIdentity.getEmail(), response.getStatus(), response.getStatusInfo().getReasonPhrase());
            log.error(message);
            throw new UserIdentityAlreadyExistsException(message);
        }

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            String message = String.format("Error creating user identity: %s, with status code %s and reason %s", userIdentity.getEmail(), response.getStatus(), response.getStatusInfo().getReasonPhrase());
            log.error(message);
            throw new UserIdentityNotCreatedException(message);
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        userRepresentation.setId(userId);
        return UserIdentityMapper.INSTANCE.toUserIdentity(userRepresentation);
    }

    /**
     * throws UserAlreadyTaggedToTenantException if user already tagged to tenantId
     *
     * @param userId   userId of user
     * @param tenantId tenantId of tenant to be tagged to user
     */
    @Override
    public void tagTenantToUser(String userId, String tenantId) {
        UserResource userResource = usersResource.get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        List<String> currentTaggedTenantIds = attributes.get(TENANT_ID_ATTR_KEY);

        if (currentTaggedTenantIds.contains(tenantId)) {
            String message = String.format("user already tagged to tenant %s", tenantId);
            log.error(message);
            throw new UserAlreadyTaggedToTenantException(message);
        }

        currentTaggedTenantIds.add(tenantId);
        userRepresentation.setAttributes(Map.of(TENANT_ID_ATTR_KEY, currentTaggedTenantIds));
        userResource.update(userRepresentation);
    }

    /**
     * throws UserNotTaggedToTenantException if user is not tagged to tenantId
     *
     * @param userId   userId of user
     * @param tenantId tenantId to be removed from user
     */
    @Override
    public void untagTenantFromUser(String userId, String tenantId) {
        UserResource userResource = usersResource.get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        List<String> currentTaggedTenantIds = Optional.ofNullable(attributes)
                .map(attr -> attr.get(TENANT_ID_ATTR_KEY))
                .orElse(Collections.emptyList());

        if (!currentTaggedTenantIds.contains(tenantId)) {
            String message = String.format("user %s is not tagged to tenant %s", userId, tenantId);
            log.info(message);
        }

        currentTaggedTenantIds.remove(tenantId);
        userRepresentation.setAttributes(Map.of(TENANT_ID_ATTR_KEY, currentTaggedTenantIds));
        userResource.update(userRepresentation);
    }

    /**
     * Idempotent — a 404 from Keycloak (user already absent) is treated as success.
     * throws UserIdentityNotDeletedException if Keycloak returns any other non-success status
     *
     * @param userId userId of user to delete
     */
    @Override
    public void deleteUser(String userId) {
        try (Response response = usersResource.delete(userId)) {
            int status = response.getStatus();
            if (status == Response.Status.NO_CONTENT.getStatusCode()) {
                log.info("Deleted user identity userId={}", userId);
                return;
            }
            if (status == Response.Status.NOT_FOUND.getStatusCode()) {
                log.info("User identity userId={} already absent — delete is a no-op", userId);
                return;
            }
            String message = String.format("Error deleting user identity: %s, with status code %s and reason %s",
                    userId, status, response.getStatusInfo().getReasonPhrase());
            log.error(message);
            throw new UserIdentityNotDeletedException(message);
        }
    }

    @Override
    public void assignRolesToUser(String userId, String... roles) {
        if (roles.length == 0)
            throw new UnexpectedTechnicalException("there must at least be 1 role assigned when using assignRolesToUser");
        List<RoleRepresentation> roleRepresentations = Arrays.stream(roles)
                .map(r -> clientResource.roles().get(r).toRepresentation())
                .toList();

        UserResource userResource = usersResource.get(userId);
        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(roleRepresentations);
        String rolesString = String.join(",", roleRepresentations.stream().map(RoleRepresentation::getName).toList());
        log.info("Client roles {} has been granted to user {}", rolesString, userId);
    }

    /**
     * Triggers Keycloak's executeActionsEmail. Caller's clientId is not specified
     * (passed null) so the realm default is used. A blank redirectUri is passed
     * as null so callers can disable the post-action redirect (and the
     * Keycloak-side Valid-Redirect-URI check that goes with it).
     * SMTP and transport failures are logged and swallowed — the surrounding
     * domain operation must not roll back.
     */
    @Override
    public void sendActionsEmail(String userId, List<String> actions, @org.jspecify.annotations.Nullable String redirectUri) {
        String resolvedRedirectUri = (redirectUri == null || redirectUri.isBlank()) ? null : redirectUri;
        // clientId is only required when redirectUri is non-null — Keycloak
        // validates the URI against the client's Valid Redirect URIs.
        String clientId = resolvedRedirectUri == null ? null : actionEmailClientId;
        try {
            usersResource.get(userId).executeActionsEmail(clientId, resolvedRedirectUri, actionTokenLifespanSeconds, actions);
            log.info("Triggered Keycloak executeActionsEmail for userId={} actions={} clientId={} redirectUri={}",
                    userId, actions, clientId, resolvedRedirectUri);
        } catch (WebApplicationException e) {
            String body = readResponseBody(e);
            log.error("Failed to trigger Keycloak executeActionsEmail for userId={} actions={} clientId={} redirectUri={}: status={} body={}",
                    userId, actions, clientId, resolvedRedirectUri, e.getResponse().getStatus(), body, e);
        } catch (RuntimeException e) {
            log.error("Failed to trigger Keycloak executeActionsEmail for userId={} actions={} clientId={} redirectUri={}: {}",
                    userId, actions, clientId, resolvedRedirectUri, e.getMessage(), e);
        }
    }

    @Override
    public boolean hasCompletedRequiredActions(String userId, List<String> requiredActions) {
        try {
            UserRepresentation user = usersResource.get(userId).toRepresentation();
            List<String> pending = Optional.ofNullable(user.getRequiredActions()).orElse(Collections.emptyList());
            return pending.stream().noneMatch(requiredActions::contains);
        } catch (WebApplicationException e) {
            log.warn("Failed to fetch Keycloak required actions for userId={}: status={} body={}",
                    userId, e.getResponse().getStatus(), readResponseBody(e), e);
            return false;
        } catch (RuntimeException e) {
            log.warn("Failed to fetch Keycloak required actions for userId={}: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    private static String readResponseBody(WebApplicationException e) {
        try {
            Response response = e.getResponse();
            if (response == null) return "<no response>";
            response.bufferEntity();
            return response.readEntity(String.class);
        } catch (RuntimeException _) {
            return "<unreadable response>";
        }
    }

}
