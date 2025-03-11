package com.reportai.www.reportapi.services.students;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import java.util.List;
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

    public Student findById(UUID id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("student does not exist"));
    }

    public List<Student> findByIds(List<UUID> ids) {
        return studentRepository.findAllById(ids);
    }

    public List<Student> getAllStudentsInInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("no institution found"));
        return institution.getStudents();
    }

}
