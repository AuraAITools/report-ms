package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public List<Student> createStudents(List<Student> students) {
        return studentRepository.saveAll(students);
    }

}
