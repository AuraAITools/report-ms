package com.reportai.www.reportapi.services;

import com.nimbusds.jose.util.Pair;
import com.reportai.www.reportapi.clients.keycloak.KeycloakAuthClient;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.dtos.responses.IndividualStatus;
import com.reportai.www.reportapi.dtos.responses.MultiStatusResponseBody;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionAlreadyExistsAbstractException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AuraAdminService {

    private final InstitutionRepository institutionRepository;

    private final AccountRepository accountRepository;
    private final KeycloakAuthClient keycloakAuthClient;

    public AuraAdminService(InstitutionRepository institutionRepository, AccountRepository accountRepository, KeycloakAuthClient keycloakAuthClient) {
        this.institutionRepository = institutionRepository;
        this.accountRepository = accountRepository;
        this.keycloakAuthClient = keycloakAuthClient;
    }

    /**
     * Onboards an institution and creates an array of admin user accounts for them
     * the email of an account for an institution cannot be used for the email of an account for another institution
     *
     * @param institution
     * @param adminAccounts
     * @return
     */
    @Transactional
    public Pair<Institution, MultiStatusResponseBody<IndividualStatus>> onboardInstitution(Institution institution, List<Account> adminAccounts) {
        // check if institution exists
        boolean institutionExists = institutionRepository.existsByEmail(institution.getEmail());

        if (institutionExists) {
            throw new HttpInstitutionAlreadyExistsAbstractException();
        }

        // create institution
        Institution createdInstitution = institutionRepository.save(institution);

        MultiStatusResponseBody<IndividualStatus> completionStatuses = new MultiStatusResponseBody<>();
        // create account or add existing account to this institution
        adminAccounts.forEach(account -> {
                    Optional<Account> existingAccount = accountRepository.findByEmail(account.getEmail());

                    if (existingAccount.isPresent()) {
                        // add new institution to this account
                        addInstitutionToExistingAccount(createdInstitution, account);
                        completionStatuses.add(
                                () -> IndividualStatus
                                        .builder()
                                        .status("201")
                                        .errorCode("")
                                        .target("accounts")
                                        .targetId(account.getId().toString())
                                        .message(String.format("successfully added institution %s to existing account %s", institution.getName(), existingAccount.get().getEmail()))
                                        .build());
                        return;
                    }

                    // if account is not present, create new keycloak account
                    UserRepresentation userRepresentation = AccountMappers.convert(account);
                    try {
                        String userId = keycloakAuthClient.createUserAccount(userRepresentation);
                        account.setUserId(userId);
                        account.setInstitutions(Set.of(createdInstitution));
                        Account createdAccount = accountRepository.save(account);
                        completionStatuses.add(
                                () -> IndividualStatus
                                        .builder()
                                        .status("201")
                                        .errorCode("")
                                        .target("accounts")
                                        .targetId(account.getId().toString())
                                        .message(String.format("successfully added institution %s to new account %s", institution.getName(), createdAccount.getEmail()))
                                        .build());
                    } catch (KeycloakUserAccountAlreadyExistsException keycloakUserAccountAlreadyExistsException) {
                        completionStatuses.add(() ->
                                IndividualStatus
                                        .builder()
                                        .status("409")
                                        .errorCode("409010")
                                        .message(String.format("keycloak account for email: %s exists but not in the database", account.getEmail()))
                                        .targetId(account.getEmail())
                                        .target("accounts")
                                        .build()
                        );
                    } catch (KeycloakUserAccountCreationException keycloakUserAccountCreationException) {
                        completionStatuses.add(() ->
                                IndividualStatus
                                        .builder()
                                        .status("400")
                                        .errorCode("400010")
                                        .message(String.format("failed to create account with email %s", account.getEmail()))
                                        .targetId(account.getEmail())
                                        .target("accounts")
                                        .build()
                        );
                    } catch (Exception exception) {
                        log.error("an unexpected error has occurred", exception);
                        completionStatuses.add(() ->
                                IndividualStatus
                                        .builder()
                                        .status("500")
                                        .errorCode("500001")
                                        .targetId(account.getEmail())
                                        .message(String.format("an unexpected error occurred when creating account with email %s", account.getEmail()))
                                        .target("accounts")
                                        .build()
                        );
                    }
                }
        );
        return Pair.of(createdInstitution, completionStatuses);
    }


    private void addInstitutionToExistingAccount(Institution institution, Account account) {
        account.getInstitutions().add(institution);
        accountRepository.save(account);
    }

}
