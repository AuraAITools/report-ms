package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ClassRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * RegistrationService helps to register/link entities to each other
 * e.g. register a student to a parent
 * There is no entity creation logic
 */
@Service
@Slf4j
public class RegistrationService {

    private final InstitutionRepository institutionRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ClassRepository classRepository;

    private final EducatorRepository educatorRepository;

    private final SubjectRepository subjectRepository;

    public RegistrationService(InstitutionRepository institutionRepository, StudentRepository studentRepository, ParentRepository parentRepository, ClassRepository classRepository, EducatorRepository educatorRepository, SubjectRepository subjectRepository) {
        this.institutionRepository = institutionRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.classRepository = classRepository;
        this.educatorRepository = educatorRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public Student registerStudentToParent(UUID studentId, UUID parentId) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);
        Optional<Parent> existingParent = parentRepository.findById(parentId);

        if(existingStudent.isEmpty()) {
            throw new NotFoundException("Student not found");
        }

        if(existingParent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }

        // register student to parent
        Student student = existingStudent.get();
        student.getParents().add(existingParent.get());
        return studentRepository.save(student);
    }




}
