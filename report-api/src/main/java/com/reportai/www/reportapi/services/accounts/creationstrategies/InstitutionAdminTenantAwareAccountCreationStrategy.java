package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;

@Slf4j
@RequiredArgsConstructor
public class InstitutionAdminTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {

    private final AccountRepository accountRepository;
    private final Account requestedAccount;
    private final UUID institutionId;
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final ClientResource clientResource;
    private final List<String> GRANTED_ROLES_ON_CREATION = List.of("institution-admin");

    public InstitutionAdminTenantAwareAccountCreationStrategy(UUID institutionId, Account requestedAccount, ClientResource clientResource, RealmResource realmResource, AccountRepository accountRepository, InstitutionRepository institutionRepository) {
        this.accountRepository = accountRepository;
        this.requestedAccount = requestedAccount;
        this.institutionId = institutionId;
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.clientResource = clientResource;
    }

    @Transactional
    @Override
    public Account createTenantAwareAccount() {
        return StrategyUtils.createTenantAwareAccount(realmResource, clientResource, institutionId, institutionRepository, accountRepository, requestedAccount, GRANTED_ROLES_ON_CREATION);
    }

}
