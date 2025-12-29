package com.aura.details.institutions.onboarding;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.services.institutions.OnboardingInstitutionService;
import com.aura.core.services.institutions.onboarding.TenantInitialDataLoader;

public class OnboardingServiceImpl implements OnboardingInstitutionService {
    private final InstitutionRepository institutionRepository;
    private final IdentityService identityService;
    private final TenantInitialDataLoader tenantInitialDataLoader;

    public OnboardingServiceImpl(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
        this.institutionRepository = institutionRepository;
        this.identityService = identityService;
        this.tenantInitialDataLoader = tenantInitialDataLoader;
    }

    @Override
    public Institution onboard(Institution institution) {
        var onboardingStrategy = new DefaultInstitutionOnboardingStrategy(institutionRepository, institution, identityService, tenantInitialDataLoader);
        return onboardingStrategy.onboard();
    }
}
