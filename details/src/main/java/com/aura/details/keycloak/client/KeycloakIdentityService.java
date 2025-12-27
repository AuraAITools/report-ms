package com.aura.details.keycloak.client;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.core.repositories.InstitutionRepository;
import java.util.List;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeycloakIdentityService implements IdentityService {
    private final Logger log = LoggerFactory.getLogger(KeycloakIdentityService.class);
    private final InstitutionRepository institutionRepository;
    private final ClientResource clientResource;

    public KeycloakIdentityService(InstitutionRepository institutionRepository, ClientResource clientResource) {
        this.institutionRepository = institutionRepository;
        this.clientResource = clientResource;
    }

    @Override
    public void createDefaultTenantAwareRoles(List<TenantAwareRole> roles) {
        // create all institution roles
        roles.forEach(role -> {
            RoleRepresentation clientRole = new RoleRepresentation();
            clientRole.setClientRole(true);
            clientRole.setDescription(role.getTenantId());
            clientRole.setName(String.format("%s_%s", role.getName(), role));
            log.info("creating client role {} for tenant {}", clientRole.getName(), role.getTenantId());
            clientResource.roles().create(clientRole);
        });
    }
}
