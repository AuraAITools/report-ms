package com.aura.details.springdata.repositories.jpa.institutions;

import com.aura.details.springdata.models.institutions.InstitutionCreationRequestModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionCreationRequestRepository extends JpaRepository<InstitutionCreationRequestModel, UUID> {

    boolean existsByInstitutionNameAndState(String institutionName, InstitutionCreationRequestModel.STATE state);

    boolean existsByInstitutionEmailAndState(String institutionEmail, InstitutionCreationRequestModel.STATE state);
}
