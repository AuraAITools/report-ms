package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.User;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * This service onboards Institutions, Students, Parents and Educators
 */
@Service
public class OnboardingService {

    private final InstitutionRepository institutionRepository;
    private final EducatorRepository educatorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    public OnboardingService(InstitutionRepository institutionRepository, EducatorRepository educatorRepository, ParentRepository parentRepository, StudentRepository studentRepository) {
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Onboard Parent to user
     * @param user
     * @return
     */
    @Transactional
    public Parent onboardToUser(Parent parent, User user) {
        Parent newParent = Parent
                .builder()
                .user(user)
                .build();
        return parentRepository.save(newParent);
    }

    /**
     * Onboard Educator to user
     * @param user
     * @return
     */
    @Transactional
    public Educator onboardToUser(Educator educator, User user) {
        Educator newEducator = Educator
                .builder()
                .user(user)
                .build();
        return educatorRepository.save(newEducator);
    }

    /**
     * Onboard Institution to user
     * @param user
     * @return institution
     */
    @Transactional
    public Institution onboardToUser(Institution institution, User user) {
        Institution newInstitution = Institution
                .builder()
                .user(user)
                .build();
        return institutionRepository.save(newInstitution);
    }

    /**
     * Onboard Student to user
     * @param user
     * @return student
     */
    @Transactional
    public Student onboardToUser(Student student, User user) {
        Student newStudent = Student
                .builder()
                .user(user)
                .build();
        return studentRepository.save(newStudent);
    }

}
