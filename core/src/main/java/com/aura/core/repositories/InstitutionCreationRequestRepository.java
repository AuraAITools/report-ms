package com.aura.core.repositories;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface InstitutionCreationRequestRepository {
    InstitutionCreationRequest save(InstitutionCreationRequest request);

    @Nullable InstitutionCreationRequest findById(String id);

    List<InstitutionCreationRequest> findAll();

    boolean existsByInstitutionNameAndState(String institutionName, InstitutionCreationRequest.STATE state);

    boolean existsByInstitutionEmailAndState(String institutionEmail, InstitutionCreationRequest.STATE state);
}
