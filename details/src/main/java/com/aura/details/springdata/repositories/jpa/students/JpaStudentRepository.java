package com.aura.details.springdata.repositories.jpa.students;

import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentRepository extends JpaRepository<StudentModel, UUID>, JpaSpecificationExecutor<StudentModel> {
}

