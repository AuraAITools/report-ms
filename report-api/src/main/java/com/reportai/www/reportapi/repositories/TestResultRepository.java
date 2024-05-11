package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestResultRepository extends JpaRepository<TestResult, UUID> {
}
