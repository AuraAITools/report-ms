package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
