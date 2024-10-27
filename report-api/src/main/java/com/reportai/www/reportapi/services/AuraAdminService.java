package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.clients.keycloak.KeycloakAuthClient;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountAlreadyExistsException;
import com.reportai.www.reportapi.clients.keycloak.exceptions.KeycloakUserAccountCreationException;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionAlreadyExistsAbstractException;
import com.reportai.www.reportapi.exceptions.http.HttpInternalServerAbstractException;
import com.reportai.www.reportapi.exceptions.http.HttpUserAccountAlreadyExistsAbstractException;
import com.reportai.www.reportapi.exceptions.http.HttpUserAccountCreationFailedAbstractException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;


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
     * Onboards an institution and creates 1 admin user for them
     * the email of an account for a institution cannot be used for the email of an account for another institution
     *
     * @param institution
     * @param adminAccount
     * @return
     */
    @Transactional
    public Account onboardInstitution(Institution institution, Account adminAccount) {
        // check if user is already present
        Optional<Account> account = accountRepository.findByEmail(adminAccount.getEmail());

        // check if account is already present
        if (account.isPresent()) {
            Set<Institution> institutions = account.get().getInstitutions();

            // if account already linked to an existing institution, stop onboarding
            if (!institutions.isEmpty()) {
                throw new HttpInstitutionAlreadyExistsAbstractException();
            }

            // link account to institution
            institution.setAccount(account.get());
            account.get().getInstitutions().add(institution);
            Institution createdInstitution = institutionRepository.save(institution);
            return account.get();
        }

        UserRepresentation userRepresentation = AccountMappers.convert(adminAccount);

        try {
            // onboard institution admin user to keycloak
            String userId = keycloakAuthClient.createUserAccount(userRepresentation);

            // save institution and account on database
            adminAccount.setUserId(userId);
            institution.setAccount(adminAccount);
            accountRepository.save(adminAccount);
            institutionRepository.save(institution);

            keycloakAuthClient.sendPendingActionsToUserEmail(userId);
        } catch (KeycloakUserAccountAlreadyExistsException keycloakUserAccountAlreadyExistsException) {
            throw new HttpUserAccountAlreadyExistsAbstractException("user account already exists", "");
        } catch (KeycloakUserAccountCreationException keycloakUserAccountAlreadyExistsException) {
            throw new HttpUserAccountCreationFailedAbstractException("user account creation cannot be processed", "");
        } catch (Exception exception) {
            log.error("an unexpected error has occured: {}", Arrays.toString(exception.getStackTrace()));
            throw new HttpInternalServerAbstractException();
        }

        return adminAccount;
    }

}
