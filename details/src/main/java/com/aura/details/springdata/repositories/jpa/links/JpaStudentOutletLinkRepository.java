package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.tables.links.StudentOutletLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentOutletLinkRepository extends JpaRepository<StudentOutletLinkTable, UUID>, JpaSpecificationExecutor<StudentOutletLinkTable> {
}

