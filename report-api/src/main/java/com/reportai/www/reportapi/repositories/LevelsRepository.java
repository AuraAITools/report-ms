package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Level;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LevelsRepository extends JpaRepository<Level, UUID>, JpaSpecificationExecutor<Level> {
    Optional<Level> findByName(String name);
}
