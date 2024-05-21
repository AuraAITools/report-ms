package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.exceptions.ResourceAlreadyExistsException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EducatorRepository extends JpaRepository<Educator, UUID> {
    Optional<Educator> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}
