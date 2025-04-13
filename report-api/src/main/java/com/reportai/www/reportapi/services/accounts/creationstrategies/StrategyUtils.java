package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.internal.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyUtils {

    private static final Logger log = LoggerFactory.getLogger(StrategyUtils.class);

    private StrategyUtils() {
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateTenantAwareAccountParams {
        private final RealmResource realmResource;
        private final ClientResource clientResource;
        private final UUID institutionId;
        private final InstitutionsService institutionsService;
        private final AccountRepository accountRepository;
        private final Account requestedAccount;
        private final List<String> grantedRoles;
    }

    @Transactional
    public static Account createTenantAwareAccount(CreateTenantAwareAccountParams params) {
        UsersResource usersResource = params.getRealmResource().users();
        UUID institutionId = params.getInstitutionId();
        RealmResource realmResource = params.getRealmResource();
        ClientResource clientResource = params.getClientResource();
        InstitutionsService institutionsService = params.getInstitutionsService();
        AccountRepository accountRepository = params.getAccountRepository();
        Account requestedAccount = params.getRequestedAccount();
        List<String> grantedRoles = params.getGrantedRoles();

        // get institution
        Institution institution = institutionsService.getCurrentInstitution();

        List<UserRepresentation> idpUserAccounts = usersResource.searchByEmail(requestedAccount.getEmail(), true);

        // each email should only be unique
        Assert.isTrue(idpUserAccounts.size() < 2);

        // TODO: look into this logic after database multitenancy change. might break
        // brand new user, create new keycloak user account and new tenant aware account
        if (idpUserAccounts.isEmpty()) {
            // create idpUserAccount in keycloak with tenant-ids as user attribute
            UserRepresentation userRepresentation = AccountMappers.convert(requestedAccount, Map.of("tenant-ids", List.of(institutionId.toString())));

            Response response = usersResource.create(userRepresentation);

            if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
                throw new KeycloakUserAccountAlreadyExistsException("keycloak user account already exists");
            }

            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new KeycloakUserAccountCreationException("user account could not be created");
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            log.info("successfully created user with userId: {}", userId);

            UserResource userResource = usersResource.get(userId);

            // add roles for created user
            List<RoleRepresentation> grantedTenantAwareRoles = grantedRoles
                    .stream()
                    .map(role -> {
                        String tenantAwareRole = String.format("%s_%s", institutionId, role);
                        return clientResource.roles().get(tenantAwareRole).toRepresentation();
                    })
                    .toList();

            userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(grantedTenantAwareRoles);
            log.info("Client roles {} has been granted to user {}", String.join(", ", grantedTenantAwareRoles.stream().map(RoleRepresentation::getName).toList()), userId);

            requestedAccount.setUserId(userId);
            return accountRepository.save(requestedAccount);
        }

        // idpUserAccount exists, check if tenantAwareAccount is already registered with idpUserAccount
        // if it exists, prevent creation of duplicate tenantAwareAccount
        if (accountRepository.findByEmail(params.getRequestedAccount().getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("account with email %s is already present in institution %s", requestedAccount.getEmail(), institution.getName()));
        }

        // idpUserAccount exists, there is already another tenantAwareAccount from another tenant registered to this idpUserAccount
        // in this case, just append tenant-id to existing user-attributes of idpUserAccount and attach tenant-specific roles
        UserRepresentation existingIdpUserAccount = idpUserAccounts.getFirst();
        requestedAccount.setUserId(existingIdpUserAccount.getId());
        List<RoleRepresentation> newGrantedTenantAwareRoles = grantedRoles
                .stream()
                .map(r -> {
                    String tenantAwareRole = String.format("%s_%s", institutionId, r);
                    log.info("fetching role {} for user {}", tenantAwareRole, existingIdpUserAccount.getId());
                    return clientResource.roles().get(tenantAwareRole).toRepresentation();
                })
                .toList();

        UserResource existingIdpUserAccountUserResource = usersResource.get(existingIdpUserAccount.getId());

        existingIdpUserAccountUserResource
                .roles()
                .clientLevel(clientResource.toRepresentation().getId())
                .add(newGrantedTenantAwareRoles);

        // append tenant-id
        addTenantIdAttributeIfNotExist(existingIdpUserAccountUserResource, institution);
        return accountRepository.save(requestedAccount);
    }

    private static void addTenantIdAttributeIfNotExist(UserResource userResource, Institution institution) {
        // add new tenant_id attributes to existing user
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        // sanity check that the existing user has the attrs set
        if (attributes == null || attributes.get("tenant-ids") == null) {
            log.error("user with no tenant found in system: {}", userRepresentation.getEmail());
            throw new RuntimeException("user with no tenant found in system");
        }

        List<String> tenantIdsAttribute = attributes.get("tenant-ids");

        // add tenant id if its not existing in list
        if (!tenantIdsAttribute.contains(institution.getId().toString())) {
            tenantIdsAttribute.add(institution.getId().toString());
            userRepresentation.setAttributes(Map.of("tenant-ids", tenantIdsAttribute));
            userResource.update(userRepresentation);
        }
    }
}
