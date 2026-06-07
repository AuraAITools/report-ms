package com.aura.core.usecases.institutions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.exceptions.InvalidInstitutionCreationRequestStateException;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OnboardInstitutionFromRequestUseCaseImpl")
class OnboardInstitutionFromRequestUseCaseImplTest {

    @Mock
    private InstitutionCreationRequestRepository institutionCreationRequestRepository;
    @InjectMocks
    private OnboardInstitutionFromRequestUseCaseImpl useCase;

    private InstitutionCreationRequest requestInState(InstitutionCreationRequest.STATE state) {
        return InstitutionCreationRequest.builder()
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
                .state(state)
                .build();
    }

    @Test
    @DisplayName("Should return the request when state is REQUESTED")
    void getRequestForFulfillmentReturnsRequestedRequest() {
        InstitutionCreationRequest request = requestInState(InstitutionCreationRequest.STATE.REQUESTED);
        when(institutionCreationRequestRepository.findById("req-1")).thenReturn(request);

        InstitutionCreationRequest result = useCase.getRequestForFulfillment("req-1");

        assertThat(result).isSameAs(request);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when request does not exist")
    void getRequestForFulfillmentThrowsWhenMissing() {
        when(institutionCreationRequestRepository.findById("missing")).thenReturn(null);

        assertThatThrownBy(() -> useCase.getRequestForFulfillment("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    @DisplayName("Should throw when request is already FULFILLED")
    void getRequestForFulfillmentThrowsWhenFulfilled() {
        InstitutionCreationRequest request = requestInState(InstitutionCreationRequest.STATE.FULFILLED);
        when(institutionCreationRequestRepository.findById("req-1")).thenReturn(request);

        assertThatThrownBy(() -> useCase.getRequestForFulfillment("req-1"))
                .isInstanceOf(InvalidInstitutionCreationRequestStateException.class)
                .hasMessageContaining("FULFILLED");
    }

    @Test
    @DisplayName("Should throw when request is REJECTED")
    void getRequestForFulfillmentThrowsWhenRejected() {
        InstitutionCreationRequest request = requestInState(InstitutionCreationRequest.STATE.REJECTED);
        when(institutionCreationRequestRepository.findById("req-1")).thenReturn(request);

        assertThatThrownBy(() -> useCase.getRequestForFulfillment("req-1"))
                .isInstanceOf(InvalidInstitutionCreationRequestStateException.class)
                .hasMessageContaining("REJECTED");
    }

    @Test
    @DisplayName("Should set state to FULFILLED and save")
    void markAsFulfilledTransitionsAndSaves() {
        InstitutionCreationRequest request = requestInState(InstitutionCreationRequest.STATE.REQUESTED);
        when(institutionCreationRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InstitutionCreationRequest result = useCase.markAsFulfilled(request);

        assertThat(result.getState()).isEqualTo(InstitutionCreationRequest.STATE.FULFILLED);
        verify(institutionCreationRequestRepository).save(request);
    }
}
