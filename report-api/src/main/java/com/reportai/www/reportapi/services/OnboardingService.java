package com.reportai.www.reportapi.services;

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

}
