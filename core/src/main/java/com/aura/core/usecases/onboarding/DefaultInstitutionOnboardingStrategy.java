package com.aura.core.usecases.onboarding;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.repositories.InstitutionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultInstitutionOnboardingStrategy implements OnboardingStrategy {

    private final Institution institution;
    private final InstitutionRepository institutionRepository;
    private final TenantInitialDataLoader tenantInitialDataLoader;

    @Override
    public Institution onboard() {
        if (institutionRepository.existsByName(institution.getName())) {
            throw new DuplicateResourceException(
                    "Institution with name '%s' already exists".formatted(institution.getName()));
        }
        if (institutionRepository.existsByEmail(institution.getEmail())) {
            throw new DuplicateResourceException(
                    "Institution with email '%s' already exists".formatted(institution.getEmail()));
        }
        UUID tenantId = UUID.randomUUID();
        institution.setId(tenantId.toString());
        institution.setTenantId(tenantId.toString());
        Institution createdInstitution = institutionRepository.save(institution);

        tenantInitialDataLoader.loadInitialDataForTenant(createdInstitution.getId());

        return createdInstitution;
    }
}
