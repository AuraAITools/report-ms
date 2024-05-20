package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    boolean existsByUserId(UUID userId);
}
