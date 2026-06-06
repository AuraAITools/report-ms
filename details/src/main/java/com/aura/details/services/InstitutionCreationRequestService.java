package com.aura.details.services;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.usecases.institutions.RequestInstitutionCreationUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstitutionCreationRequestService {

    private final RequestInstitutionCreationUseCase requestInstitutionCreationUseCase;

    @Transactional
    public InstitutionCreationRequest request(InstitutionCreationRequest request) {
        return requestInstitutionCreationUseCase.request(request);
    }
}
