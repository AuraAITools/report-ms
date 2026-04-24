package com.aura.details.springdata.repositories.jpa.schools;

import com.aura.details.springdata.models.schools.SchoolModel;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSchoolRepository extends JpaRepository<SchoolModel, UUID>, JpaSpecificationExecutor<SchoolModel> {
    @Nullable SchoolModel findByNameAndTenantId(String name, String tenantId);
}

