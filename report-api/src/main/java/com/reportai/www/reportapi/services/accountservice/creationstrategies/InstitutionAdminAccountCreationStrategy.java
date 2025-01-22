package com.reportai.www.reportapi.services.accountservice.creationstrategies;

import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;


import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class InstitutionAdminAccountCreationStrategy implements AccountCreationStrategy {

    private final AccountRepository accountRepository;
    private final Account requestedAccount;
    private final UUID institutionId;
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final ClientResource clientResource;


    // TODO: refactor to only use realm resource and client resource, dont need keycloakClient
    public InstitutionAdminAccountCreationStrategy(UUID institutionId, Account requestedAccount, ClientResource clientResource, RealmResource realmResource, AccountRepository accountRepository, InstitutionRepository institutionRepository) {
        this.accountRepository = accountRepository;
        this.requestedAccount = requestedAccount;
        this.institutionId = institutionId;
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.clientResource = clientResource;
    }

    @Transactional
    @Override
    public Account createAccount() {
        UsersResource usersResource = realmResource.users();

        // get institution
        Institution institution = institutionRepository.getReferenceById(institutionId);
        Optional<Account> optionalAccount = accountRepository.findByEmail(requestedAccount.getEmail());

        // keycloak account already exists
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            boolean accountInSameInstitution = !account.getInstitutions().stream().filter(i -> institutionId.equals(i.getId())).toList().isEmpty();

            // if keycloak account is in same institution, reject account creation request
            if (accountInSameInstitution) {
                throw new KeycloakUserAccountAlreadyExistsException(format("account with email %s exist in institution id %s", requestedAccount.getEmail(), institutionId));
            }

            // if user exist in different institution
            // add roles from new institution into existing user
            RoleRepresentation existingRole = clientResource.roles().get(String.format("%s_institution-admin", this.institutionId)).toRepresentation();
            UserResource userResource = usersResource.get(account.getUserId());
            userResource.roles()
                    .clientLevel(clientResource.toRepresentation().getId())
                    .add(List.of(existingRole));

            // add new tenant_id attributes to existing user if not already added
            addTenantIdAttributeIfNotExist(userResource, institution);
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

        // add default roles for created user
        RoleRepresentation existingRole = clientResource.roles().get(String.format("%s_institution-admin", this.institutionId)).toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(List.of(existingRole));
        log.info("Client role {} has been granted to user {}", existingRole.getName(), userId);

        requestedAccount.setUserId(userId);
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
