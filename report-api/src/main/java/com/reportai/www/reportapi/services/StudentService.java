package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.StudentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService extends CRUDServiceSupport<Student,UUID> {

    public StudentService(StudentRepository studentRepository) {
        super(studentRepository);
    }
}
