package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.personas.InstitutionAdminPersona;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionAdminPersonaRepository;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;

@Slf4j
public class InstitutionAdminTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {


    private final List<String> GRANTED_ROLES_ON_CREATION = List.of("institution-admin");
    private final CreateInstitutionAdminTenantAwareAccountParams params;

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateInstitutionAdminTenantAwareAccountParams {
        private final AccountRepository accountRepository;
        private final Account requestedAccount;
        private final UUID institutionId;
        private final RealmResource realmResource;
        private final InstitutionsService institutionsService;
        private final ClientResource clientResource;
        private final InstitutionAdminPersonaRepository institutionAdminPersonaRepository;
    }

    public InstitutionAdminTenantAwareAccountCreationStrategy(CreateInstitutionAdminTenantAwareAccountParams params) {
        this.params = params;
    }

    @Transactional
    @Override
    public Account createTenantAwareAccount() {
        Account account = StrategyUtils.createTenantAwareAccount(
                StrategyUtils.CreateTenantAwareAccountParams
                        .builder()
                        .realmResource(params.getRealmResource())
                        .clientResource(params.getClientResource())
                        .institutionId(params.getInstitutionId())
                        .institutionsService(params.getInstitutionsService())
                        .accountRepository(params.getAccountRepository())
                        .requestedAccount(params.getRequestedAccount())
                        .grantedRoles(GRANTED_ROLES_ON_CREATION).build());
        InstitutionAdminPersona institutionAdminPersona = InstitutionAdminPersona
                .builder()
                .tenantId(params.getInstitutionId().toString())
                .account(account)
                .build();
        params.institutionAdminPersonaRepository.save(institutionAdminPersona);
        return account;
    }

}
