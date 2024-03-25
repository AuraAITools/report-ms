package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentById(UUID id){
        Optional<Student> foundStudent = this.studentRepository.findById(id);
        if (foundStudent.isEmpty()){
            throw new NotFoundException("student not found");
        }
        return foundStudent.get();
    }

    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }
}
