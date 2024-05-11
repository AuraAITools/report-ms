package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {
}
