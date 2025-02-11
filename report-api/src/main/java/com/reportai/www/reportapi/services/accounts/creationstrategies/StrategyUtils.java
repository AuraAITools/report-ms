package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrategyUtils {

    private static final Logger log = LoggerFactory.getLogger(StrategyUtils.class);

    private StrategyUtils() {
    }

    @Transactional
    public static Account createTenantAwareAccount(RealmResource realmResource, ClientResource clientResource, UUID institutionId, InstitutionRepository institutionRepository, AccountRepository accountRepository, Account requestedAccount, List<String> grantedRoles) {
        UsersResource usersResource = realmResource.users();
        requestedAccount.setTenantId(institutionId.toString()); // TODO: arguably should be in converter

//        Persona
        // get institution
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(HttpInstitutionNotFoundException::new);

        // fetch all TenantAwareAccounts in All tenants/institution
        List<Account> accountsInAllTenants = accountRepository.findAllByEmail(requestedAccount.getEmail());

        // brand new user, create new keycloak user account and new tenant aware account
        if (accountsInAllTenants.isEmpty()) {
            // create user account in keycloak
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
            // TODO: see if we can add roles after email is verified
            List<RoleRepresentation> grantedTenantAwareRoles = grantedRoles
                    .stream()
                    .map(role -> {
                        String tenantAwareRole = String.format("%s_%s", institutionId, role);
                        return clientResource.roles().get(tenantAwareRole).toRepresentation();
                    }).toList();

            userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(grantedTenantAwareRoles);
            log.info("Client roles {} has been granted to user {}", String.join(", ", grantedTenantAwareRoles.stream().map(RoleRepresentation::getName).toList()), userId);

            requestedAccount.setUserId(userId);
            requestedAccount.setInstitution(institution);
            return accountRepository.save(requestedAccount);
        }

        // if TenantAwareAccount exists in tenant, throw error
        boolean tenantAwareAccountExistsInTenant = !accountsInAllTenants
                .stream()
                .filter(account -> institution.getId().equals(account.getInstitution().getId()))
                .toList()
                .isEmpty();

        if (tenantAwareAccountExistsInTenant) {
            throw new ResourceAlreadyExistsException(String.format("account with email %s is already present in institution %s", requestedAccount.getEmail(), institution.getName()));
        }

        // if TenantAwareAccount exists in another tenant/institution
        // skip creation of keycloak user account
        // add tenant_id and tenant aware roles into existing keycloak account user
        // and create tenantAwareAccount in current tenant
        List<UserRepresentation> users = usersResource.searchByEmail(requestedAccount.getEmail(), true);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException(String.format("keycloak user account by email %s not found", requestedAccount.getEmail()));
        }

        // TODO: specify a more detailed unchecked exception
        if (users.size() > 1) {
            throw new RuntimeException("SYSTEM_IRREGULARITY: System found 2 keycloak user accounts with the same email");
        }

        UserRepresentation targetUser = users.getFirst();

        // add tenant aware roles to targetUser
        List<RoleRepresentation> newGrantedTenantAwareRoles = grantedRoles.stream().map(r -> {
            String tenantAwareRole = String.format("%s_%s", institutionId, r);
            log.info("fetching role {} for user {}", tenantAwareRole, targetUser.getId());
            return clientResource
                    .roles()
                    .get(tenantAwareRole)
                    .toRepresentation();
        }).toList();

        UserResource targetUserResource = usersResource.get(targetUser.getId());

        // TODO: see if you can grant roles after email verification will be better
        targetUserResource
                .roles()
                .clientLevel(clientResource.toRepresentation().getId())
                .add(newGrantedTenantAwareRoles);

        addTenantIdAttributeIfNotExist(targetUserResource, institution);
        requestedAccount.setInstitution(institution);
        requestedAccount.setUserId(targetUser.getId());
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
