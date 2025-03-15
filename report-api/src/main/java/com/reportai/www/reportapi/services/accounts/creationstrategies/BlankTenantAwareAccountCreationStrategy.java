package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;

public class BlankTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {
    private CreateBlankTenantAwareAccountParams params;
    private final List<String> GRANTED_ROLES_ON_CREATION = Collections.emptyList();

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateBlankTenantAwareAccountParams {
        private final AccountRepository accountRepository;
        private final Account requestedAccount;
        private final UUID institutionId;
        private final RealmResource realmResource;
        private final InstitutionsService institutionsService;
        private final ClientResource clientResource;
    }


    public BlankTenantAwareAccountCreationStrategy(@NonNull CreateBlankTenantAwareAccountParams params) {
        this.params = params;
    }

    @Override
    public Account createTenantAwareAccount() {
        return StrategyUtils.createTenantAwareAccount(
                StrategyUtils.CreateTenantAwareAccountParams
                        .builder()
                        .realmResource(params.getRealmResource())
                        .clientResource(params.getClientResource())
                        .institutionId(params.getInstitutionId())
                        .institutionsService(params.getInstitutionsService())
                        .accountRepository(params.getAccountRepository())
                        .requestedAccount(params.getRequestedAccount())
                        .grantedRoles(GRANTED_ROLES_ON_CREATION)
                        .build());
    }
}
