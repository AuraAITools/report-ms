package com.aura.details.springdata.repositories.jpa.subjects;

import com.aura.details.springdata.models.subjects.SubjectLevelLinkTemplateModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLevelLinkTemplateRepository extends JpaRepository<SubjectLevelLinkTemplateModel, UUID>, JpaSpecificationExecutor<SubjectLevelLinkTemplateModel> {
}

