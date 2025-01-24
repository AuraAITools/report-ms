package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public static Account createAccount(RealmResource realmResource, ClientResource clientResource, UUID institutionId, InstitutionRepository institutionRepository, AccountRepository accountRepository, Account requestedAccount, List<String> grantedRoles) {
        UsersResource usersResource = realmResource.users();
        requestedAccount.setTenantId(institutionId.toString());

        // get institution
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(HttpInstitutionNotFoundException::new);
        Optional<Account> optionalAccount = accountRepository.findByEmail(requestedAccount.getEmail());

        // keycloak account already exists
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            boolean accountInSameInstitution = !account.getInstitutions().stream().filter(i -> institutionId.equals(i.getId())).toList().isEmpty();

            // if keycloak account is in same institution, reject account creation request
            if (accountInSameInstitution) {
                throw new KeycloakUserAccountAlreadyExistsException(String.format("account with email %s exist in institution id %s", requestedAccount.getEmail(), institutionId));
            }

            // if user exist in different institution
            // if user exist in different institution
            // add roles from new institution into existing user
            UserResource userResource = usersResource.get(account.getUserId());
            grantedRoles.forEach(r -> {
                String tenantAwareRole = String.format("%s_%s", institutionId, r);
                log.info("Granting role {} to user {}", tenantAwareRole, account.getUserId());
                RoleRepresentation existingRole = clientResource.roles().get(tenantAwareRole).toRepresentation();
                userResource.roles()
                        .clientLevel(clientResource.toRepresentation().getId())
                        .add(List.of(existingRole));
            });

            addTenantIdAttributeIfNotExist(userResource, institution);
            account.getInstitutions().add(institution);
            accountRepository.save(account);
            return account;
        }


        // create account in keycloak
        UserRepresentation userRepresentation = AccountMappers.convert(requestedAccount, Map.of("tenant-ids", List.of(institutionId.toString())));

        // create user in keycloak
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
        List<RoleRepresentation> listOfRoles = grantedRoles.stream().map(role -> {
            String tenantAwareRole = String.format("%s_%s", institutionId, role);
            return clientResource.roles().get(tenantAwareRole).toRepresentation();
        }).toList();

        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(listOfRoles);
        log.info("Client roles {} has been granted to user {}", listOfRoles, userId);


        requestedAccount.setUserId(userId);
        requestedAccount.setInstitutions(List.of(institution));
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
