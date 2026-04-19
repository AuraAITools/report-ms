package com.aura.core.usecases.onboarding;

import com.aura.core.clients.GroupAuthorizationService;
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
    private final GroupAuthorizationService groupAuthorizationService;

    @Override
    public Institution onboard() {
        UUID tenantId = UUID.randomUUID();
        institution.setId(tenantId.toString());
        institution.setTenantId(tenantId.toString());
        Institution createdInstitution = institutionRepository.save(institution);

        tenantInitialDataLoader.loadInitialDataForTenant(createdInstitution.getId());

        String institutionId = createdInstitution.getId();
        String resourceName = "institution:" + institutionId;
        String groupName = "institution:" + institutionId + ":admin";
        String policyName = "institution-" + institutionId + "-admin-policy";
        String permissionName = "institution-" + institutionId + "-permission";

        // 1. Register UMA resource
        resourceAuthorizationService.registerResource(
                resourceName,
                INSTITUTION_RESOURCE_TYPE,
                null,
                Set.of("view", "manage", "delegate", "create")
        );

        // 2. Create group for institution admins
        groupAuthorizationService.createGroup(groupName);

        // 3. Create group policy
        groupAuthorizationService.createGroupPolicy(policyName, groupName);

        // 4. Create resource permission linking policy to resource
        groupAuthorizationService.createResourcePermission(
                permissionName,
                resourceName,
                Set.of("view", "manage", "delegate", "create"),
                Set.of(policyName)
        );

        return createdInstitution;
    }
}
