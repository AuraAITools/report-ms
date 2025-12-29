package com.aura.details.springdata.repositories.jpa.subjects;

import com.aura.details.springdata.tables.subjects.SubjectLevelLinkTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLevelLinkTemplateRepository extends JpaRepository<SubjectLevelLinkTemplateTable, UUID>, JpaSpecificationExecutor<SubjectLevelLinkTemplateTable> {
}

