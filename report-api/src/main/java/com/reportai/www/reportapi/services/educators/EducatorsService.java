package com.reportai.www.reportapi.services.educators;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EducatorsService {
    private final EducatorRepository educatorRepository;
    private final InstitutionRepository institutionRepository;

    public EducatorsService(EducatorRepository educatorRepository, InstitutionRepository institutionRepository) {
        this.educatorRepository = educatorRepository;
        this.institutionRepository = institutionRepository;
    }

    public Educator createEducatorForInstitution(Educator educator, UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Educator not found"));
        educator.setInstitution(institution);
        return educatorRepository.save(educator);
    }

}
