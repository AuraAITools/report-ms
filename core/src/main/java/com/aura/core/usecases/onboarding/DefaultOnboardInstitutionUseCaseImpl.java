package com.aura.core.usecases.onboarding;

import com.aura.core.clients.GroupAuthorizationService;
import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultOnboardInstitutionUseCaseImpl implements OnboardInstitutionUseCase {
    private final InstitutionRepository institutionRepository;
    private final TenantInitialDataLoader tenantInitialDataLoader;
    private final ResourceAuthorizationService resourceAuthorizationService;
    private final GroupAuthorizationService groupAuthorizationService;

    @Override
    public Institution onboard(Institution institution) {
        var onboardingStrategy = new DefaultInstitutionOnboardingStrategy(
                institution, institutionRepository, tenantInitialDataLoader,
                resourceAuthorizationService, groupAuthorizationService);
        return onboardingStrategy.onboard();
    }
}
