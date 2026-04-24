package com.aura.details.springdata.repositories.jpa.materials;

import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMaterialRepository extends JpaRepository<MaterialModel, UUID>, JpaSpecificationExecutor<MaterialModel> {
}

