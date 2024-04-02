package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.commons.CRUDServiceSupport;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentService extends CRUDServiceSupport<Student,UUID> {

    public StudentService(StudentRepository studentRepository) {
        super(studentRepository);
    }
}
