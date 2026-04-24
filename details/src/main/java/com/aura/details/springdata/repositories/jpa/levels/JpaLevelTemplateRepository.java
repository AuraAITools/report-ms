package com.aura.details.springdata.repositories.jpa.levels;

import com.aura.details.springdata.models.levels.LevelTemplateModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelTemplateRepository extends JpaRepository<LevelTemplateModel, UUID>, JpaSpecificationExecutor<LevelTemplateModel> {
}

