package com.aura.details.springdata.repositories.jpa.tests;

import com.aura.details.springdata.tables.tests.TestResultTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestResultRepository extends JpaRepository<TestResultTable, UUID>, JpaSpecificationExecutor<TestResultTable> {
}

