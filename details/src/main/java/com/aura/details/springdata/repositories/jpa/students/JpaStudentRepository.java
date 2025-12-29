package com.aura.details.springdata.repositories.jpa.students;

import com.aura.details.springdata.tables.students.StudentTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentRepository extends JpaRepository<StudentTable, UUID>, JpaSpecificationExecutor<StudentTable> {
}

