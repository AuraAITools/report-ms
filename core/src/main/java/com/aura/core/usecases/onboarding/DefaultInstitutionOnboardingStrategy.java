package com.aura.core.usecases.onboarding;

import com.aura.core.clients.ResourceAuthorizationService;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultInstitutionOnboardingStrategy implements OnboardingStrategy {
    private static final String INSTITUTION_RESOURCE_TYPE = "urn:aura:institution";

    private final Institution institution;
    private final InstitutionRepository institutionRepository;
    private final TenantInitialDataLoader tenantInitialDataLoader;
    private final ResourceAuthorizationService resourceAuthorizationService;

    @Override
    public Institution onboard() {
        UUID tenantId = UUID.randomUUID();
        institution.setId(tenantId.toString());
        institution.setTenantId(tenantId.toString());
        Institution createdInstitution = institutionRepository.save(institution);
        tenantInitialDataLoader.loadInitialDataForTenant(createdInstitution.getId().toString());
        resourceAuthorizationService.registerResource(
                "institution:" + createdInstitution.getId(),
                INSTITUTION_RESOURCE_TYPE,
                null,
                Set.of("view", "manage", "delegate", "create")
        );
        return createdInstitution;
    }
}
