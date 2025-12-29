package com.aura.details.springdata.repositories.jpa.institutions;

import com.aura.details.springdata.tables.institutions.InstitutionTable;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionRepository extends JpaRepository<InstitutionTable, UUID>, JpaSpecificationExecutor<InstitutionTable> {
    Window<InstitutionTable> findAllBy(ScrollPosition scrollPosition, Limit limit, Sort sort);
}

