package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.clients.keycloak.KeycloakAuthClient;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionAlreadyExistsAbstractException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuraAdminService {

    private final InstitutionRepository institutionRepository;

    private final KeycloakAuthClient keycloakAuthClient;

    private final AccountRepository accountRepository;

    @Autowired
    public AuraAdminService(InstitutionRepository institutionRepository, KeycloakAuthClient keycloakAuthClient, AccountRepository accountRepository) {
        this.institutionRepository = institutionRepository;
        this.keycloakAuthClient = keycloakAuthClient;
        this.accountRepository = accountRepository;
    }

    /**
     * Onboards an institution and creates 1 admin user for them
     *
     * @param institution
     * @param institutionAdminUser
     * @return
     */
    @Transactional
    public String onboardInstitution(Institution institution, UserRepresentation institutionAdminUser) {
        // create institution
        if (institutionRepository.existsByEmail(institution.getEmail())) {
            throw new HttpInstitutionAlreadyExistsAbstractException();
        }

        Institution createdInstitution = institutionRepository.save(institution);
        //TODO: save account also
//        accountRepository.save();

        // onboard institution admin user to keycloak
        String userId = keycloakAuthClient.createDefaultUserAccount(institutionAdminUser);
        keycloakAuthClient.sendPendingActionsToUserEmail(userId);
        return userId;

    }

}
