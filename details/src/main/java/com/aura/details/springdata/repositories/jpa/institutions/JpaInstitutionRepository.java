package com.aura.details.springdata.repositories.jpa.institutions;

import com.aura.details.springdata.models.institutions.InstitutionModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionRepository extends JpaRepository<InstitutionModel, UUID>, JpaSpecificationExecutor<InstitutionModel> {
}

