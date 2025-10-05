package com.aura.details.repositories.jpa.tests;

import com.aura.details.tables.tests.TestTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTestRepository extends JpaRepository<TestTable, UUID>, JpaSpecificationExecutor<TestTable> {
}

