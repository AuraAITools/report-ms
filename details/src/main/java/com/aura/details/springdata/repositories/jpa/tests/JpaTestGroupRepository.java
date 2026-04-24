package com.aura.details.springdata.repositories.jpa.tests;

import com.aura.details.springdata.models.tests.TestGroupModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestGroupRepository extends JpaRepository<TestGroupModel, UUID>, JpaSpecificationExecutor<TestGroupModel> {
}

