package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Parent;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    public OnboardingService(InstitutionRepository institutionRepository, EducatorRepository educatorRepository, ParentRepository parentRepository, StudentRepository studentRepository) {
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
    }

    // TODO: add account exist checks
    public Parent onboard(Parent parent) {
        return parentRepository.save(parent);
    }

    public Student onboard(Student student) {
        return studentRepository.save(student);
    }

    public Institution onboard(Institution institution) {
        return institutionRepository.save(institution);

    }

    public Educator onboard(Educator educator) {
        return educatorRepository.save(educator);
    }

}
