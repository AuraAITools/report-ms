package com.aura.details.repositories.jpa.outlets;

import com.aura.details.tables.outlets.OutletTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOutletRepository extends JpaRepository<OutletTable, UUID>, JpaSpecificationExecutor<OutletTable> {
}

