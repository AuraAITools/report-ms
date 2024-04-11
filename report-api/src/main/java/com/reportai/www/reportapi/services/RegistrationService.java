package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
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

    @Transactional
    public Educator registerEducatorToInstitution(UUID educatorId, UUID institutionId) {
        Optional<Educator> existingEducator = educatorRepository.findById(educatorId);
        Optional<Institution> existingInstitution = institutionRepository.findById(institutionId);

        if(existingEducator.isEmpty()) {
            throw new NotFoundException("Educator not found");
        }

        if(existingInstitution.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        // register educator to parent
        Educator educator = existingEducator.get();
        educator.getInstitutions().add(existingInstitution.get());
        return educatorRepository.save(educator);
    }

    @Transactional
    public Parent registerParentToInstitution(UUID parentId, UUID institutionId) {
        Optional<Parent> existingParent = parentRepository.findById(parentId);
        Optional<Institution> existingInstitution = institutionRepository.findById(institutionId);

        if(existingParent.isEmpty()) {
            throw new NotFoundException("Parent not found");
        }

        if(existingInstitution.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        // register parent to parent
        Parent parent = existingParent.get();
        parent.getInstitutions().add(existingInstitution.get());
        return parentRepository.save(parent);
    }

    @Transactional
    public Class registerClassToInstitution(UUID classId, UUID institutionId) {
        Optional<Class> existingClass = classRepository.findById(classId);
        Optional<Institution> existingInstitution = institutionRepository.findById(institutionId);

        if(existingClass.isEmpty()) {
            throw new NotFoundException("Class not found");
        }

        if(existingInstitution.isEmpty()) {
            throw new NotFoundException("Institution not found");
        }

        // register class to class
        Class foundClass = existingClass.get();
        foundClass.setInstitution(existingInstitution.get());
        return classRepository.save(foundClass);
    }

    @Transactional
    public Student registerStudentToClass(UUID studentId, UUID classId) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);
        Optional<Class> existingClass = classRepository.findById(classId);

        if(existingStudent.isEmpty()) {
            throw new NotFoundException("Student not found");
        }

        if(existingClass.isEmpty()) {
            throw new NotFoundException("Class not found");
        }

        // register class to class
        Student foundStudent = existingStudent.get();
        foundStudent.getClasses().add(existingClass.get());
        return studentRepository.save(foundStudent);
    }

    @Transactional
    public Subject registerSubjectToClass(UUID subjectId, UUID classId) {
        Optional<Subject> existingSubject = subjectRepository.findById(subjectId);
        Optional<Class> existingClass = classRepository.findById(classId);

        if(existingSubject.isEmpty()) {
            throw new NotFoundException("Subject not found");
        }

        if(existingClass.isEmpty()) {
            throw new NotFoundException("Class not found");
        }

        // register class to class
        Subject foundSubject = existingSubject.get();
        foundSubject.setMappedClass(existingClass.get());
        return subjectRepository.save(foundSubject);
    }




}
