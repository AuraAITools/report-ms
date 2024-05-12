package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.User;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * This service onboards Institutions, Students, Parents and Educators
 */
@Service
@Slf4j
public class OnboardingService {

    private final InstitutionRepository institutionRepository;
    private final EducatorRepository educatorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    public OnboardingService(InstitutionRepository institutionRepository, EducatorRepository educatorRepository, ParentRepository parentRepository, StudentRepository studentRepository, UserRepository userRepository) {
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public User onboard(User user) {
        return userRepository.save(user);
    }
}
