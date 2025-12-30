package com.aura.details.springdata.repositories.jpa.institutions;

import com.aura.details.springdata.tables.institutions.InstitutionTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionRepository extends JpaRepository<InstitutionTable, UUID>, JpaSpecificationExecutor<InstitutionTable> {
}

