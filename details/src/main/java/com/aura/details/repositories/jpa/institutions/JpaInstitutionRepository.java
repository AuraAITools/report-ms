package com.aura.details.repositories.jpa.institutions;

import com.aura.details.tables.institutions.InstitutionTable;
import java.util.UUID;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInstitutionRepository extends JpaRepository<InstitutionTable, UUID>, JpaSpecificationExecutor<InstitutionTable> {
    Window<InstitutionTable> findAllBy(ScrollPosition scrollPosition, Limit limit, Sort sort);

    @Procedure("onboard_tenant")
    void loadInitialDataForTenant(String tenantId);
}

