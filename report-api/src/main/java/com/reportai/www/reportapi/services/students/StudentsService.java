package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentsService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentsService(StudentRepository studentRepository) {
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
