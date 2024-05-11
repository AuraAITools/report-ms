package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ObjectiveRepository extends JpaRepository<Objective, UUID> {
}
