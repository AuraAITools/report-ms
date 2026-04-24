package com.aura.details.springdata.repositories.jpa.educators;

import com.aura.details.springdata.models.educators.EducatorModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorRepository extends JpaRepository<EducatorModel, UUID>, JpaSpecificationExecutor<EducatorModel> {
}

