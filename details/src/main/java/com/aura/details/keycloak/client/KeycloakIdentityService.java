package com.aura.details.keycloak.client;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.details.keycloak.client.mappers.UserIdentityMapper;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
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

public class KeycloakIdentityService implements IdentityService {
    private final Logger log = LoggerFactory.getLogger(KeycloakIdentityService.class);
    private final ClientResource clientResource;
    private final UsersResource usersResource;

    public KeycloakIdentityService(ClientResource clientResource, UsersResource usersResource) {
        this.clientResource = clientResource;
        this.usersResource = usersResource;
    }

    @Override
    public void createDefaultTenantAwareRoles(List<TenantAwareRole> roles) {
        // create all institution roles
        roles.forEach(role -> {
            RoleRepresentation clientRole = new RoleRepresentation();
            clientRole.setClientRole(true);
            clientRole.setDescription(role.getTenantId());
            clientRole.setName(String.format("%s_%s", role.getTenantId(), role.getName()));
            log.info("creating client role {} for tenant {}", clientRole.getName(), role.getTenantId());
            clientResource.roles().create(clientRole);
        });
    }

    @Override
    public List<UserIdentity> findByEmail(String email) {
        List<UserRepresentation> userRepresentations = usersResource.searchByEmail(email, true);
        return userRepresentations.stream().map(UserIdentityMapper.INSTANCE::toUserIdentity).toList();
    }

    @Override
    public UserIdentity createUserIdentity(UserIdentity userIdentity, String tenantId) {

        Map<String, List<String>> extAttributes = new HashMap<>();
        extAttributes.put("tenant-ids", List.of(tenantId));
        UserRepresentation userRepresentation = UserIdentityMapper.INSTANCE.toUserRepresentation(userIdentity);
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            log.error("createUser() failed with status code {}", response.getStatus());
            throw new RuntimeException(response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        userRepresentation.setId(userId);
        return UserIdentityMapper.INSTANCE.toUserIdentity(userRepresentation);
    }

    @Override
    public void addTenantToUser(String userId, String tenantId) {
        UserResource userResource = usersResource.get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        // sanity check that the existing user has the attrs set
        if (attributes == null || attributes.get("tenant-ids") == null) {
            log.error("user with no tenant found in system: {}", userRepresentation.getEmail());
            throw new RuntimeException("user with no tenant found in system");
        }

        List<String> tenantIdsAttribute = attributes.get("tenant-ids");

        // add tenant id if its not existing in list
        if (!tenantIdsAttribute.contains(tenantId)) {
            tenantIdsAttribute.add(tenantId);
            userRepresentation.setAttributes(Map.of("tenant-ids", tenantIdsAttribute));
            userResource.update(userRepresentation);
        }
    }

    @Override
    public void assignRolesToUser(String userId, String... roles) {
        List<RoleRepresentation> roleRepresentations = Arrays.stream(roles)
                .map(r -> clientResource.roles().get(r).toRepresentation())
                .toList();

        UserResource userResource = usersResource.get(userId);
        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(roleRepresentations);
        log.info("Client roles {} has been granted to user {}", String.join(", ", roleRepresentations.stream().map(RoleRepresentation::getName).toList()), userId);
    }

}
