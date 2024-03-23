package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Class;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.User;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.ClassRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class RegistrationService {

    private final EducatorRepository educatorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    private final InstitutionRepository institutionRepository;

    public RegistrationService(EducatorRepository educatorRepository, ParentRepository parentRepository, StudentRepository studentRepository, ClassRepository classRepository, SubjectRepository subjectRepository, UserRepository userRepository, InstitutionRepository institutionRepository) {
        this.educatorRepository = educatorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public Institution registerInstitution(Institution entity, User user) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        return institutionRepository.save(entity);
    }

    @Transactional
    public Educator registerEducatorWithInstitution(Educator entity, User user, UUID institutionId) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitutions(List.of(institution.get()));
        return educatorRepository.save(entity);
    }


    @Transactional
    public Parent registerParentWithInstitution(Parent entity, User user , UUID institutionId) {
        log.info("gerer"+institutionId.toString());
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        entity.setInstitutions(List.of(institution.get()));
        return parentRepository.save(entity);
    }


    @Transactional
    public Student registerStudentWithInstitution(Student entity, User user, UUID institutionId) {
        User savedUser = userRepository.save(user);
        entity.setUser(savedUser);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitutions(List.of(institution.get()));
        return studentRepository.save(entity);
    }

    @Transactional
    public Class registerClassWithInstitution(Class entity, UUID institutionId) {
        Class newClass = classRepository.save(entity);
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitution(institution.get());
        return classRepository.save(entity);
    }


    @Transactional
    public Subject registerSubjectWithInstitution(Subject entity, UUID institutionId) {
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new NotFoundException("institution not found");
        }
        entity.setInstitution(institution.get());
        return subjectRepository.save(entity);
    }
}
