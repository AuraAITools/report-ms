package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Institution;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstitutionRepository extends JpaRepository<Institution, UUID>, JpaSpecificationExecutor<Institution> {
    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<Institution> findByEmail(String email);
}
