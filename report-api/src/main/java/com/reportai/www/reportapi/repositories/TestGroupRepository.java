package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.TestGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestGroupRepository extends JpaRepository<TestGroup, UUID> {
}
