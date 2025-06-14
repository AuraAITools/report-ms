package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.School;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SchoolRepository extends JpaRepository<School, UUID>, JpaSpecificationExecutor<School> {
}