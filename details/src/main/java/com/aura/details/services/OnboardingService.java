package com.aura.details.services;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardInstitutionUseCase onboardInstitutionUseCase;

    @Transactional
    public Institution onboard(Institution institution) {
        return onboardInstitutionUseCase.onboard(institution);
    }
}
