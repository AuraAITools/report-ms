package com.aura.details.springdata.repositories.jpa.subjects;

import com.aura.details.springdata.models.subjects.SubjectTemplateModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectTemplateRepository extends JpaRepository<SubjectTemplateModel, UUID>, JpaSpecificationExecutor<SubjectTemplateModel> {
}

