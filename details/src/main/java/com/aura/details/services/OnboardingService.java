package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.usecases.onboarding.OnboardInstitutionUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardInstitutionUseCase onboardInstitutionUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Institution onboard(Institution institution) {
        Institution created = onboardInstitutionUseCase.onboard(institution);
        List<RelationshipAuthorizationService.Relationship> relationships = List.of(
                new RelationshipAuthorizationService.Relationship("institution", created.getId(), "user_group", "institution_user_group", created.getId()),
                new RelationshipAuthorizationService.Relationship("institution_user_group", created.getId(), "parent", "system_user_group", "aura"),
                new RelationshipAuthorizationService.Relationship("institution", created.getId(), "parent", "system", "aura")
        );
        relationshipService.writeRelationships(relationships);
        return created;
    }
}
