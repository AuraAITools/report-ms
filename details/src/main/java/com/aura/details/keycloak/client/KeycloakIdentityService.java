package com.aura.details.keycloak.client;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.exceptions.UnexpectedTechnicalException;
import com.aura.core.exceptions.UserIdentityAlreadyExistsException;
import com.aura.core.exceptions.UserIdentityNotCreatedException;
import com.aura.details.keycloak.client.exceptions.UserAlreadyTaggedToTenantException;
import com.aura.details.keycloak.client.mappers.UserIdentityMapper;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.aura.core.clients.UserIdentityAttributeKeys.TENANT_ID_ATTR_KEY;

public class KeycloakIdentityService implements IdentityService {
    private final Logger log = LoggerFactory.getLogger(KeycloakIdentityService.class);
    private final ClientResource clientResource;
    private final UsersResource usersResource;

    public KeycloakIdentityService(ClientResource clientResource, UsersResource usersResource) {
        this.clientResource = clientResource;
        this.usersResource = usersResource;
    }

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

}
