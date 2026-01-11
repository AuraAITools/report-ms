package com.aura.details.springdata.repositories.jpa.levels;

import com.aura.details.springdata.tables.levels.LevelTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelRepository extends JpaRepository<LevelTable, UUID>, JpaSpecificationExecutor<LevelTable> {
    LevelTable findByNameAndTenantId(String name, String tenantId);
}

