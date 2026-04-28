package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardInstitutionUseCase onboardInstitutionUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Institution onboard(Institution institution) {
        Institution created = onboardInstitutionUseCase.onboard(institution);
        relationshipService.writeRelationship(
                "institution", created.getId(), "parent_system", "system", "global");
        return created;
    }
}
