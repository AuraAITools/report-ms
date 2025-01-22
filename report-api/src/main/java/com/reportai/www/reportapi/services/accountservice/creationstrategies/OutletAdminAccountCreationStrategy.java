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

@Slf4j
@RequiredArgsConstructor
public class OutletAdminAccountCreationStrategy implements AccountCreationStrategy {
    private final ClientResource clientResource;
    private final AccountRepository accountRepository;
    private final Account requestedAccount;
    private final UUID institutionId;
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;


    public OutletAdminAccountCreationStrategy(UUID institutionId, Account requestedAccount, ClientResource clientResource, RealmResource realmResource, AccountRepository accountRepository, InstitutionRepository institutionRepository) {
        this.clientResource = clientResource;
        this.accountRepository = accountRepository;
        this.requestedAccount = requestedAccount;
        this.institutionId = institutionId;
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    @Override
    public Account createAccount() {
        UsersResource usersResource = realmResource.users();

        // get institution
        Institution institution = institutionRepository.getReferenceById(institutionId);
        Optional<Account> optionalAccount = accountRepository
                .findByEmail(requestedAccount.getEmail());

        // keycloak account already exists
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            boolean accountInSameInstitution = !account.getInstitutions().stream().filter(i -> institutionId.equals(i.getId())).toList().isEmpty();

            // if keycloak account is in same institution, reject account creation request
            if (accountInSameInstitution) {
                throw new KeycloakUserAccountAlreadyExistsException(String.format("account with email %s exist in institution id %s", requestedAccount.getEmail(), institutionId));
            }

            // if user exist in different institution
            // TODO: merge roles of this institution into the same keycloak account
            UserResource userResource = usersResource.get(account.getUserId());
            userResource.roles().clientLevel("local-next").add(List.of(new RoleRepresentation("name", "description", true)));

            return account;
        }


        // create account in keycloak
        // TODO: set conventionally named client roles once theyre up
        UserRepresentation userRepresentation = AccountMappers.convert(requestedAccount, Map.of("tenant-id", List.of(institutionId.toString())));
        //        userRepresentation.setClientRoles();

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

        requestedAccount.setUserId(userId);
        return accountRepository.save(requestedAccount);
    }
}
