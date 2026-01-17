package com.aura.details.springdata.repositories.jpa.schools;

import com.aura.details.springdata.tables.schools.SchoolTable;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSchoolRepository extends JpaRepository<SchoolTable, UUID>, JpaSpecificationExecutor<SchoolTable> {
    @Nullable SchoolTable findByNameAndTenantId(String name, String tenantId);
}

