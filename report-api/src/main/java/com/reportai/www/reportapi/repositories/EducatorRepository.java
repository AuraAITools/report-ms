package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Educator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducatorRepository extends JpaRepository<Educator, UUID> {
}
