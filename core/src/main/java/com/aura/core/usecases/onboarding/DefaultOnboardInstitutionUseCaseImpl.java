package com.aura.core.usecases.onboarding;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;

public class DefaultOnboardInstitutionUseCaseImpl implements OnboardInstitutionUseCase {
    private final InstitutionRepository institutionRepository;
    private final IdentityService identityService;
    private final TenantInitialDataLoader tenantInitialDataLoader;

    public DefaultOnboardInstitutionUseCaseImpl(InstitutionRepository institutionRepository, IdentityService identityService, TenantInitialDataLoader tenantInitialDataLoader) {
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
