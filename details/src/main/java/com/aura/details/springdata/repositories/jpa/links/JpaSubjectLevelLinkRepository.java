package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.tables.links.SubjectLevelLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLevelLinkRepository extends JpaRepository<SubjectLevelLinkTable, UUID>, JpaSpecificationExecutor<SubjectLevelLinkTable> {
}

