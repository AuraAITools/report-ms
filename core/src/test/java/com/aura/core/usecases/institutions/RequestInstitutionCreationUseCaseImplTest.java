package com.aura.core.usecases.institutions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.exceptions.InstitutionCreationRequestConflictException;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import com.aura.core.repositories.InstitutionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequestInstitutionCreationUseCaseImpl")
class RequestInstitutionCreationUseCaseImplTest {

    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private InstitutionCreationRequestRepository institutionCreationRequestRepository;
    @InjectMocks
    private RequestInstitutionCreationUseCaseImpl useCase;

    private InstitutionCreationRequest validRequest() {
        return InstitutionCreationRequest.builder()
                .institutionName("Acme Tutors")
                .institutionEmail("contact@acme.edu")
                .institutionContactNumber("+6512345678")
                .institutionAddress("1 Acme Way")
                .institutionUen("UEN-001")
                .requesterFirstName("Alice")
                .requesterLastName("Lee")
                .requesterEmail("alice@acme.edu")
                .requesterContactNumber("+6587654321")
                .build();
    }

    @Test
    @DisplayName("Should save with generated id and REQUESTED state when no conflicts")
    void requestSucceedsWhenNoConflicts() {
        InstitutionCreationRequest input = validRequest();
        when(institutionRepository.existsByName(input.getInstitutionName())).thenReturn(false);
        when(institutionRepository.existsByEmail(input.getInstitutionEmail())).thenReturn(false);
        when(institutionCreationRequestRepository.existsByInstitutionNameAndState(
                input.getInstitutionName(), InstitutionCreationRequest.STATE.REQUESTED)).thenReturn(false);
        when(institutionCreationRequestRepository.existsByInstitutionEmailAndState(
                input.getInstitutionEmail(), InstitutionCreationRequest.STATE.REQUESTED)).thenReturn(false);
        when(institutionCreationRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InstitutionCreationRequest saved = useCase.request(input);

        assertThat(saved.getId()).isNotBlank();
        assertThat(saved.getState()).isEqualTo(InstitutionCreationRequest.STATE.REQUESTED);
        verify(institutionCreationRequestRepository).save(saved);
    }

    @Test
    @DisplayName("Should throw conflict when an institution with same name already exists")
    void requestFailsWhenInstitutionNameExists() {
        InstitutionCreationRequest input = validRequest();
        when(institutionRepository.existsByName(input.getInstitutionName())).thenReturn(true);

        assertThatThrownBy(() -> useCase.request(input))
                .isInstanceOf(InstitutionCreationRequestConflictException.class)
                .hasMessageContaining("name");

        verify(institutionCreationRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw conflict when an institution with same email already exists")
    void requestFailsWhenInstitutionEmailExists() {
        InstitutionCreationRequest input = validRequest();
        when(institutionRepository.existsByName(input.getInstitutionName())).thenReturn(false);
        when(institutionRepository.existsByEmail(input.getInstitutionEmail())).thenReturn(true);

        assertThatThrownBy(() -> useCase.request(input))
                .isInstanceOf(InstitutionCreationRequestConflictException.class)
                .hasMessageContaining("email");

        verify(institutionCreationRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw conflict when a pending request with the same name exists")
    void requestFailsWhenPendingRequestNameExists() {
        InstitutionCreationRequest input = validRequest();
        when(institutionRepository.existsByName(input.getInstitutionName())).thenReturn(false);
        when(institutionRepository.existsByEmail(input.getInstitutionEmail())).thenReturn(false);
        when(institutionCreationRequestRepository.existsByInstitutionNameAndState(
                input.getInstitutionName(), InstitutionCreationRequest.STATE.REQUESTED)).thenReturn(true);

        assertThatThrownBy(() -> useCase.request(input))
                .isInstanceOf(InstitutionCreationRequestConflictException.class)
                .hasMessageContaining("pending");

        verify(institutionCreationRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw conflict when a pending request with the same email exists")
    void requestFailsWhenPendingRequestEmailExists() {
        InstitutionCreationRequest input = validRequest();
        when(institutionRepository.existsByName(input.getInstitutionName())).thenReturn(false);
        when(institutionRepository.existsByEmail(input.getInstitutionEmail())).thenReturn(false);
        when(institutionCreationRequestRepository.existsByInstitutionNameAndState(
                input.getInstitutionName(), InstitutionCreationRequest.STATE.REQUESTED)).thenReturn(false);
        when(institutionCreationRequestRepository.existsByInstitutionEmailAndState(
                input.getInstitutionEmail(), InstitutionCreationRequest.STATE.REQUESTED)).thenReturn(true);

        assertThatThrownBy(() -> useCase.request(input))
                .isInstanceOf(InstitutionCreationRequestConflictException.class)
                .hasMessageContaining("pending");

        verify(institutionCreationRequestRepository, never()).save(any());
    }
}
