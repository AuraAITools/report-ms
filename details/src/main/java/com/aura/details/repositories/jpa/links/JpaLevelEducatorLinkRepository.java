package com.aura.details.repositories.jpa.links;

import com.aura.details.tables.links.LevelEducatorLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelEducatorLinkRepository extends JpaRepository<LevelEducatorLinkTable, UUID>, JpaSpecificationExecutor<LevelEducatorLinkTable> {
}

