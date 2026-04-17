package com.aura.details.keycloak.client;

import com.aura.core.clients.ResourceAuthorizationService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

@Slf4j
@RequiredArgsConstructor
public class KeycloakResourceAuthorizationService implements ResourceAuthorizationService {
    private final ClientResource clientResource;

    @Override
    public void registerResource(String name, String type, String ownerId, Set<String> scopes) {
        ResourceRepresentation resource = new ResourceRepresentation();
        resource.setName(name);
        resource.setType(type);
        resource.setOwner(ownerId);
        resource.setOwnerManagedAccess(true);
        resource.setScopes(scopes.stream()
                .map(ScopeRepresentation::new)
                .collect(Collectors.toSet()));
        clientResource.authorization().resources().create(resource);
        log.info("registered UMA resource '{}' of type '{}' owned by '{}'", name, type, ownerId);
    }

    @Override
    public void deleteResource(String name) {
        List<ResourceRepresentation> resources = clientResource.authorization()
                .resources().findByName(name);
        if (!resources.isEmpty()) {
            String resourceId = resources.getFirst().getId();
            clientResource.authorization().resources().resource(resourceId).remove();
            log.info("deleted UMA resource '{}'", name);
        } else {
            log.warn("UMA resource '{}' not found for deletion", name);
        }
    }
}
