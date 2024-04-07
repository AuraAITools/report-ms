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

    /**
     * Onboard Parent to platform, if parent has an existing user, just create and link them together
     *
     * @param parent
     * @return parent that was created on linked
     */
    @Transactional
    public Parent onboard(Parent parent) {
        User existingUser = userRepository.findDistinctByEmail(parent.getUser().getEmail());

        if (isNull(existingUser)) {
            User newUser = userRepository.save(parent.getUser());
            parent.setUser(newUser);
            return parentRepository.save(parent);
        }
        parent.setUser(existingUser);
        return parentRepository.save(parent);
    }

    @Transactional
    public Student onboard(Student student) {
        User existingUser = userRepository.findDistinctByEmail(student.getUser().getEmail());

        if (isNull(existingUser)) {
            User newUser = userRepository.save(student.getUser());
            student.setUser(newUser);
            return studentRepository.save(student);
        }
        student.setUser(existingUser);
        return studentRepository.save(student);
    }

    @Transactional
    public Educator onboard(Educator educator) {
        User existingUser = userRepository.findDistinctByEmail(educator.getUser().getEmail());

        if (isNull(existingUser)) {
            User newUser = userRepository.save(educator.getUser());
            educator.setUser(newUser);
            return educatorRepository.save(educator);
        }
        educator.setUser(existingUser);
        return educatorRepository.save(educator);
    }

    @Transactional
    public Institution onboard(Institution institution) {
        User existingUser = userRepository.findDistinctByEmail(institution.getUser().getEmail());

        if (isNull(existingUser)) {
            User newUser = userRepository.save(institution.getUser());
            institution.setUser(newUser);
            return institutionRepository.save(institution);
        }
        institution.setUser(existingUser);
        return institutionRepository.save(institution);
    }
}
