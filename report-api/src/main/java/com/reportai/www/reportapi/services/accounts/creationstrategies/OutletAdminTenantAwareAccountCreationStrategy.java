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
public class OutletAdminTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {
    private final ClientResource clientResource;
    private final AccountRepository accountRepository;
    private final Account requestedAccount;
    private final UUID institutionId;
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final String OUTLET_ADMIN_ROLE_TEMPLATE = "%s_outlet-admin";
    private final List<String> grantedRoles;

    public OutletAdminTenantAwareAccountCreationStrategy(UUID institutionId, Account requestedAccount, ClientResource clientResource, RealmResource realmResource, AccountRepository accountRepository, InstitutionRepository institutionRepository, List<String> outletIds) {
        this.clientResource = clientResource;
        this.accountRepository = accountRepository;
        this.requestedAccount = requestedAccount;
        this.institutionId = institutionId;
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.grantedRoles = outletIds
                .stream()
                .map(outletId -> String.format(OUTLET_ADMIN_ROLE_TEMPLATE, outletId))
                .toList();
    }

    @Transactional
    @Override
    public Account createTenantAwareAccount() {
        return StrategyUtils.createTenantAwareAccount(realmResource, clientResource, institutionId, institutionRepository, accountRepository, requestedAccount, grantedRoles);
    }
}
