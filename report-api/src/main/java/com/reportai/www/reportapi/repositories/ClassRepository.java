package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassRepository extends JpaRepository<Class, UUID> {
}
