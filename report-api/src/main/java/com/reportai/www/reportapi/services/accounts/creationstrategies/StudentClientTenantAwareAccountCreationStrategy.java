package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;

@Slf4j
@RequiredArgsConstructor
public class StudentClientTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {

    private final AccountRepository accountRepository;
    private final Account requestedAccount;
    private final UUID institutionId;
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final ClientResource clientResource;
    private final List<String> GRANTED_ROLES_ON_CREATION = List.of("student-report-mobile");
    private final StudentClientPersonaRepository studentClientPersonaRepository;
    private final StudentClientPersona.RELATIONSHIP relationship;

    public StudentClientTenantAwareAccountCreationStrategy(UUID institutionId, Account requestedAccount, ClientResource clientResource, RealmResource realmResource, AccountRepository accountRepository, InstitutionRepository institutionRepository, StudentClientPersonaRepository studentClientPersonaRepository, StudentClientPersona.RELATIONSHIP relationship) {
        this.accountRepository = accountRepository;
        this.requestedAccount = requestedAccount;
        this.institutionId = institutionId;
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.clientResource = clientResource;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.relationship = relationship;
    }

    @Transactional
    @Override
    public Account createTenantAwareAccount() {
        Account account = StrategyUtils.createTenantAwareAccount(realmResource, clientResource, institutionId, institutionRepository, accountRepository, requestedAccount, GRANTED_ROLES_ON_CREATION);
        StudentClientPersona studentClientPersona = StudentClientPersona
                .builder()
                .relationship(relationship)
                .tenantId(institutionId.toString())
                .account(account)
                .build();
        studentClientPersonaRepository.save(studentClientPersona);
        return account;
    }
}
