package com.aura.core.usecases.onboarding;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOnboardInstitutionUseCaseImpl implements OnboardInstitutionUseCase {
    private final InstitutionRepository institutionRepository;
    private final TenantInitialDataLoader tenantInitialDataLoader;

    @Override
    public Institution onboard(Institution institution) {
        OnboardingStrategy onboardingStrategy = new DefaultInstitutionOnboardingStrategy(
                institution, institutionRepository, tenantInitialDataLoader);
        return onboardingStrategy.onboard();
    }
}
