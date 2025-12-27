package com.aura.details.repositories.jpa.schools;

import com.aura.details.tables.schools.SchoolTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSchoolTemplateRepository extends JpaRepository<SchoolTemplateTable, UUID>, JpaSpecificationExecutor<SchoolTemplateTable> {
}

