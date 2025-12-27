package com.aura.details.institutions.onboarding;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.TenantAwareRole;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.institutions.onboarding.OnboardingStrategy;
import com.aura.core.institutions.onboarding.TenantInitialDataLoader;
import com.aura.core.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public class DefaultInstitutionOnboardingStrategy implements OnboardingStrategy {
    private final Institution institution;
    private final InstitutionRepository institutionRepository;
    private final IdentityService identityService;
    private final TenantInitialDataLoader tenantInitialDataLoader;
    private final List<String> ALL_INSTITUTION_ROLES = List.of("institution-admin", "student-report-mobile", "educator-report-mobile");


    public DefaultInstitutionOnboardingStrategy(InstitutionRepository institutionRepository, Institution institution, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        this.institution = institution;
        this.institutionRepository = institutionRepository;
        this.identityService = identityService;
        this.tenantInitialDataLoader = tenantInitialDataLoader;
    }

    @Transactional
    @Override
    public Institution onboard() {
        UUID tenantId = UUID.randomUUID();
        institution.setId(tenantId);
        institution.setTenantId(tenantId.toString());
        Institution createdInstitution = institutionRepository.save(institution);
        tenantInitialDataLoader.loadInitialDataForTenant(createdInstitution.getId().toString());
        List<TenantAwareRole> tenantAwareRoles = ALL_INSTITUTION_ROLES.stream().map(rolename -> {
                    TenantAwareRole role = TenantAwareRole
                            .builder()
                            .name(rolename)
                            .tenantId(institution.getId().toString())
                            .description(institution.getId().toString())
                            .build();
                    return role;
                })
                .toList();
        identityService.createDefaultTenantAwareRoles(tenantAwareRoles);
        return createdInstitution;
    }
}
