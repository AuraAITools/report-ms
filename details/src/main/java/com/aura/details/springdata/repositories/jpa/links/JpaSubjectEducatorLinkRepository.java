package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.tables.links.SubjectEducatorLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectEducatorLinkRepository extends JpaRepository<SubjectEducatorLinkTable, UUID>, JpaSpecificationExecutor<SubjectEducatorLinkTable> {
}

