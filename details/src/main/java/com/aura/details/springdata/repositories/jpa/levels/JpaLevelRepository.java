package com.aura.details.springdata.repositories.jpa.levels;

import com.aura.details.springdata.models.levels.LevelModel;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelRepository extends JpaRepository<LevelModel, UUID>, JpaSpecificationExecutor<LevelModel> {
    @Nullable LevelModel findByNameAndTenantId(String name, String tenantId);
}

