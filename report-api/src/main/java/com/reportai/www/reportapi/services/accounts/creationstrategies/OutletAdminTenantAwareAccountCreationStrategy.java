package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.OutletAdminPersonaRepository;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;

@Slf4j
@RequiredArgsConstructor
public class OutletAdminTenantAwareAccountCreationStrategy implements TenantAwareAccountCreationStrategy {

    private final String OUTLET_ADMIN_ROLE_TEMPLATE = "%s_outlet-admin";
    private final List<String> grantedRoles;
    private final CreateOutletAdminTenantAwareAccountParams params;

    public OutletAdminTenantAwareAccountCreationStrategy(CreateOutletAdminTenantAwareAccountParams params) {
        this.params = params;
        this.grantedRoles = params.getOutletIds()
                .stream()
                .map(outletId -> String.format(OUTLET_ADMIN_ROLE_TEMPLATE, outletId))
                .toList();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class CreateOutletAdminTenantAwareAccountParams {
        private final ClientResource clientResource;
        private final AccountRepository accountRepository;
        private final Account requestedAccount;
        private final UUID institutionId;
        private final RealmResource realmResource;
        private final InstitutionsService institutionsService;
        private final OutletsService outletsService;
        private final OutletAdminPersonaRepository outletAdminPersonaRepository;
        private final List<UUID> outletIds;
    }

    @Transactional
    @Override
    public Account createTenantAwareAccount() {
        List<Outlet> outlets = params.getOutletsService().findByIds(params.getOutletIds());
        Account account = StrategyUtils.createTenantAwareAccount(
                StrategyUtils.CreateTenantAwareAccountParams.builder()
                        .realmResource(params.getRealmResource())
                        .clientResource(params.getClientResource())
                        .institutionId(params.getInstitutionId())
                        .institutionsService(params.getInstitutionsService())
                        .accountRepository(params.getAccountRepository())
                        .requestedAccount(params.getRequestedAccount())
                        .grantedRoles(grantedRoles).build());
        OutletAdminPersona outletAdminPersona = OutletAdminPersona
                .builder()
                .account(account)
                .build();
        OutletAdminPersona createdOutletAdminPersona = params.getOutletAdminPersonaRepository().save(outletAdminPersona);
        outlets.forEach(outlet -> params.getOutletsService().addOutletAdminToOutlet(outlet.getId(), createdOutletAdminPersona.getId()));
        return account;
    }
}
