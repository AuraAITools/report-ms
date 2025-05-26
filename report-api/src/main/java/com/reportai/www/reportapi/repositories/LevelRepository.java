package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Level;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LevelRepository extends JpaRepository<Level, UUID>, JpaSpecificationExecutor<Level> {
    Optional<Level> findById(UUID id);

    List<Level> findAllById(Iterable<UUID> ids);

    Optional<Level> findByName(String name);

    Optional<Level> findByNameAndTenantId(String name, String tenantId);

    List<Level> findByNameIn(List<String> names);
}
