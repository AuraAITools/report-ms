package com.aura.details.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstitutionCreationRequestService")
class InstitutionCreationRequestServiceTest {

    @Mock
    private RequestInstitutionCreationUseCase requestInstitutionCreationUseCase;
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
}
