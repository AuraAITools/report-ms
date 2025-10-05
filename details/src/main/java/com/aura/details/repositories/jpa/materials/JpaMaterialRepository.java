package com.aura.details.repositories.jpa.materials;

import com.aura.details.tables.materials.MaterialTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMaterialRepository extends JpaRepository<MaterialTable, UUID>, JpaSpecificationExecutor<MaterialTable> {
}

