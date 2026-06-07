package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeatureState;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.usecases.institutions.OnboardInstitutionFromRequestUseCase;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstitutionCreationRequestService {

    private final RequestInstitutionCreationUseCase requestInstitutionCreationUseCase;
    private final OnboardInstitutionFromRequestUseCase onboardInstitutionFromRequestUseCase;
    private final OnboardingService onboardingService;
    private final AccountService accountService;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public InstitutionCreationRequest request(InstitutionCreationRequest request) {
        return requestInstitutionCreationUseCase.request(request);
    }

    @Transactional
    public Institution onboardFromRequest(String institutionCreationRequestId) {
        InstitutionCreationRequest request = onboardInstitutionFromRequestUseCase
                .getRequestForFulfillment(institutionCreationRequestId);

        Institution institution = Institution.builder()
                .name(request.getInstitutionName())
                .email(request.getInstitutionEmail())
                .contactNumber(request.getInstitutionContactNumber())
                .address(request.getInstitutionAddress())
                .uen(request.getInstitutionUen())
                .state(Institution.STATE.PENDING)
                .build();
        Institution onboardedInstitution = onboardingService.onboard(institution);

        Account adminAccount = Account.builder()
                .tenantId(onboardedInstitution.getId())
                .email(request.getRequesterEmail())
                .firstName(request.getRequesterFirstName())
                .lastName(request.getRequesterLastName())
                .contact(request.getRequesterContactNumber())
                .parentFeatureState(AccountFeatureState.NOT_ONBOARDED)
                .educatorFeatureState(AccountFeatureState.NOT_ONBOARDED)
                .staffFeatureState(AccountFeatureState.NOT_ONBOARDED)
                .build();
        Account createdAccount = accountService.createAccount(adminAccount);

        relationshipService.writeRelationships(List.of(
                new RelationshipAuthorizationService.Relationship(
                        "institution_user_group", onboardedInstitution.getId(),
                        "admin", "user", createdAccount.getUserId()),
                new RelationshipAuthorizationService.Relationship(
                        "institution_user_group", onboardedInstitution.getId(),
                        "group_admin", "user", createdAccount.getUserId())
        ));

        onboardInstitutionFromRequestUseCase.markAsFulfilled(request);

        return onboardedInstitution;
    }
}
