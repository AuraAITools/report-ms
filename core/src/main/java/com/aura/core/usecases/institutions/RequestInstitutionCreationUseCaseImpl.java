package com.aura.core.usecases.institutions;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.core.exceptions.InstitutionCreationRequestConflictException;
import com.aura.core.repositories.InstitutionCreationRequestRepository;
import com.aura.core.repositories.InstitutionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestInstitutionCreationUseCaseImpl implements RequestInstitutionCreationUseCase {

    private final InstitutionRepository institutionRepository;
    private final InstitutionCreationRequestRepository institutionCreationRequestRepository;

    @Override
    public InstitutionCreationRequest request(InstitutionCreationRequest request) {
        String name = request.getInstitutionName();
        String email = request.getInstitutionEmail();

        if (institutionRepository.existsByName(name)) {
            throw new InstitutionCreationRequestConflictException(
                    "an institution with name '" + name + "' already exists");
        }
        if (institutionRepository.existsByEmail(email)) {
            throw new InstitutionCreationRequestConflictException(
                    "an institution with email '" + email + "' already exists");
        }
        if (institutionCreationRequestRepository.existsByInstitutionNameAndState(
                name, InstitutionCreationRequest.STATE.REQUESTED)) {
            throw new InstitutionCreationRequestConflictException(
                    "a pending creation request with institution name '" + name + "' already exists");
        }
        if (institutionCreationRequestRepository.existsByInstitutionEmailAndState(
                email, InstitutionCreationRequest.STATE.REQUESTED)) {
            throw new InstitutionCreationRequestConflictException(
                    "a pending creation request with institution email '" + email + "' already exists");
        }

        request.setId(UUID.randomUUID().toString());
        request.setState(InstitutionCreationRequest.STATE.REQUESTED);
        return institutionCreationRequestRepository.save(request);
    }
}
