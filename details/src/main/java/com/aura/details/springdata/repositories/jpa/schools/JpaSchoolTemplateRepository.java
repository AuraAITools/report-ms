package com.aura.details.springdata.repositories.jpa.schools;

import com.aura.details.springdata.models.schools.SchoolTemplateModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSchoolTemplateRepository extends JpaRepository<SchoolTemplateModel, UUID>, JpaSpecificationExecutor<SchoolTemplateModel> {
}

