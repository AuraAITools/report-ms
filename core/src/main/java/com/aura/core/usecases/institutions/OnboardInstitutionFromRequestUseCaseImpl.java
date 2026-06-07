package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.exceptions.InvalidInstitutionCreationRequestStateException;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnboardInstitutionFromRequestUseCaseImpl implements OnboardInstitutionFromRequestUseCase {

    private final InstitutionCreationRequestRepository institutionCreationRequestRepository;

    @Override
    public InstitutionCreationRequest getRequestForFulfillment(String requestId) {
        InstitutionCreationRequest request = Optional.ofNullable(institutionCreationRequestRepository.findById(requestId))
                .orElseThrow(() -> new ResourceNotFoundException("InstitutionCreationRequest", requestId));
        if (request.getState() != InstitutionCreationRequest.STATE.REQUESTED) {
            throw new InvalidInstitutionCreationRequestStateException(
                    "InstitutionCreationRequest " + requestId + " is in state " + request.getState()
                            + "; expected " + InstitutionCreationRequest.STATE.REQUESTED);
        }
        return request;
    }

    @Override
    public InstitutionCreationRequest markAsFulfilled(InstitutionCreationRequest request) {
        request.setState(InstitutionCreationRequest.STATE.FULFILLED);
        return institutionCreationRequestRepository.save(request);
    }
}
