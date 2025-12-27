package com.aura.details.repositories.jpa.subjects;

import com.aura.details.tables.subjects.SubjectTable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectRepository extends JpaRepository<SubjectTable, UUID>, JpaSpecificationExecutor<SubjectTable> {
    Optional<SubjectTable> findByName(String subjectName);
}

