package com.aura.details.repositories.jpa.educators;

import com.aura.details.tables.educators.EducatorTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorRepository extends JpaRepository<EducatorTable, UUID>, JpaSpecificationExecutor<EducatorTable> {
}

