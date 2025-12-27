package com.aura.details.repositories.jpa.levels;

import com.aura.details.tables.levels.LevelTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelRepository extends JpaRepository<LevelTable, UUID>, JpaSpecificationExecutor<LevelTable> {
}

