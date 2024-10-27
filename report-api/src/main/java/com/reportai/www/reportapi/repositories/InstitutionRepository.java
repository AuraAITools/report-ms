package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstitutionRepository extends JpaRepository<Institution, UUID> {
    boolean existsByEmail(String email);
}
