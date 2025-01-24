package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

}
