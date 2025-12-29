package com.aura.details.springdata.repositories.jpa.educators;

import com.aura.details.springdata.tables.educators.EducatorTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorRepository extends JpaRepository<EducatorTable, UUID>, JpaSpecificationExecutor<EducatorTable> {
}

