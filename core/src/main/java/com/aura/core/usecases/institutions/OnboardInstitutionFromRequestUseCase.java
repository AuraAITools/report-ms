package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.InstitutionCreationRequest;

public interface OnboardInstitutionFromRequestUseCase {
    InstitutionCreationRequest getRequestForFulfillment(String requestId);

    InstitutionCreationRequest markAsFulfilled(InstitutionCreationRequest request);
}
