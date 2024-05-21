package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstitutionRepository extends JpaRepository<Institution, UUID> {
    Optional<Institution> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}
