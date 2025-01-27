package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.http.HttpInstitutionNotFoundException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentsService {
    private final StudentRepository studentRepository;

    private final InstitutionRepository institutionRepository;

    @Autowired
    public StudentsService(StudentRepository studentRepository, InstitutionRepository institutionRepository) {
        this.studentRepository = studentRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Student createStudentForInstitution(Student student, UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(HttpInstitutionNotFoundException::new);
        student.setInstitution(institution);
        return studentRepository.save(student);
    }

    public Student findById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("student does not exist"));
    }

}
