package com.aura.details.springdata.repositories.jpa.levels;

import com.aura.details.springdata.tables.levels.LevelTemplateTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelTemplateRepository extends JpaRepository<LevelTemplateTable, UUID>, JpaSpecificationExecutor<LevelTemplateTable> {
}

