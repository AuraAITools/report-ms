package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeatureState;
import com.aura.core.entities.institutions.Institution;
import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.usecases.institutions.OnboardInstitutionFromRequestUseCase;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstitutionCreationRequestService")
class InstitutionCreationRequestServiceTest {

    @Mock
    private RequestInstitutionCreationUseCase requestInstitutionCreationUseCase;
    @Mock
    private OnboardInstitutionFromRequestUseCase onboardInstitutionFromRequestUseCase;
    @Mock
    private OnboardingService onboardingService;
    @Mock
    private AccountService accountService;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @InjectMocks
    private InstitutionCreationRequestService service;

    @Test
    @DisplayName("Should delegate to the request use case and return its result")
    void requestDelegatesToUseCase() {
        InstitutionCreationRequest input = InstitutionCreationRequest.builder()
                .institutionName("Acme").build();
        InstitutionCreationRequest saved = InstitutionCreationRequest.builder()
                .id("req-1")
                .institutionName("Acme")
                .state(InstitutionCreationRequest.STATE.REQUESTED)
                .build();
        when(requestInstitutionCreationUseCase.request(input)).thenReturn(saved);

        InstitutionCreationRequest result = service.request(input);

        assertThat(result).isSameAs(saved);
        verify(requestInstitutionCreationUseCase).request(input);
    }

    @Test
    @DisplayName("Should onboard institution, create admin account, and mark request fulfilled")
    void onboardFromRequestFullFlow() {
        InstitutionCreationRequest request = InstitutionCreationRequest.builder()
                .id("req-1")
                .institutionName("Acme Tutors")
                .institutionEmail("contact@acme.edu")
                .institutionContactNumber("+6512345678")
                .institutionAddress("1 Acme Way")
                .institutionUen("UEN-001")
                .requesterFirstName("Alice")
                .requesterLastName("Lee")
                .requesterEmail("alice@acme.edu")
                .requesterContactNumber("+6587654321")
                .state(InstitutionCreationRequest.STATE.REQUESTED)
                .build();
        Institution onboarded = Institution.builder()
                .id("inst-1")
                .tenantId("inst-1")
                .name("Acme Tutors")
                .email("contact@acme.edu")
                .build();
        Account createdAccount = Account.builder()
                .id("acc-1")
                .userId("user-1")
                .tenantId("inst-1")
                .build();
        when(onboardInstitutionFromRequestUseCase.getRequestForFulfillment("req-1")).thenReturn(request);
        when(onboardingService.onboard(any(Institution.class))).thenReturn(onboarded);
        when(accountService.createAccount(any(Account.class))).thenReturn(createdAccount);

        Institution result = service.onboardFromRequest("req-1");

        assertThat(result).isSameAs(onboarded);

        ArgumentCaptor<Institution> institutionCaptor = ArgumentCaptor.forClass(Institution.class);
        verify(onboardingService).onboard(institutionCaptor.capture());
        Institution passedInstitution = institutionCaptor.getValue();
        assertThat(passedInstitution.getName()).isEqualTo("Acme Tutors");
        assertThat(passedInstitution.getEmail()).isEqualTo("contact@acme.edu");
        assertThat(passedInstitution.getContactNumber()).isEqualTo("+6512345678");
        assertThat(passedInstitution.getAddress()).isEqualTo("1 Acme Way");
        assertThat(passedInstitution.getUen()).isEqualTo("UEN-001");
        assertThat(passedInstitution.getState()).isEqualTo(Institution.STATE.PENDING);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService).createAccount(accountCaptor.capture());
        Account passedAccount = accountCaptor.getValue();
        assertThat(passedAccount.getTenantId()).isEqualTo("inst-1");
        assertThat(passedAccount.getEmail()).isEqualTo("alice@acme.edu");
        assertThat(passedAccount.getFirstName()).isEqualTo("Alice");
        assertThat(passedAccount.getLastName()).isEqualTo("Lee");
        assertThat(passedAccount.getContact()).isEqualTo("+6587654321");
        assertThat(passedAccount.getParentFeatureState()).isEqualTo(AccountFeatureState.NOT_ONBOARDED);
        assertThat(passedAccount.getEducatorFeatureState()).isEqualTo(AccountFeatureState.NOT_ONBOARDED);
        assertThat(passedAccount.getStaffFeatureState()).isEqualTo(AccountFeatureState.NOT_ONBOARDED);

        verify(relationshipService).writeRelationships(List.of(
                new RelationshipAuthorizationService.Relationship(
                        "institution_user_group", "inst-1", "admin", "user", "user-1"),
                new RelationshipAuthorizationService.Relationship(
                        "institution_user_group", "inst-1", "group_admin", "user", "user-1")
        ));

        verify(onboardInstitutionFromRequestUseCase).markAsFulfilled(request);
    }
}
