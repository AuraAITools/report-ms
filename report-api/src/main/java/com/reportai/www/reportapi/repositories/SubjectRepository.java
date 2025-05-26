package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Subject;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    List<Subject> findByNameIn(List<String> names);
}
