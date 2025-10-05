package com.aura.details.repositories.jpa.tests;

import com.aura.details.tables.tests.TestGroupTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestGroupRepository extends JpaRepository<TestGroupTable, UUID>, JpaSpecificationExecutor<TestGroupTable> {
}

