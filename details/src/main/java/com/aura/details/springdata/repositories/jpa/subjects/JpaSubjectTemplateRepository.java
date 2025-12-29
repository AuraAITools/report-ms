package com.aura.details.springdata.repositories.jpa.subjects;

import com.aura.details.springdata.tables.subjects.SubjectTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectTemplateRepository extends JpaRepository<SubjectTemplateTable, UUID>, JpaSpecificationExecutor<SubjectTemplateTable> {
}

