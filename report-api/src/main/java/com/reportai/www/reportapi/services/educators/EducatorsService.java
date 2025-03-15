package com.reportai.www.reportapi.services.educators;

import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class EducatorsService implements BaseServiceTemplate<Educator, UUID> {
    private final EducatorRepository educatorRepository;
    private final InstitutionsService institutionsService;
    private final OutletsService outletsService;

    private final EducatorClientPersonaRepository educatorClientPersonaRepository;

    @Autowired
    public EducatorsService(EducatorRepository educatorRepository, InstitutionsService institutionsService, OutletsService outletsService, EducatorClientPersonaRepository educatorClientPersonaRepository) {
        this.educatorRepository = educatorRepository;
        this.institutionsService = institutionsService;
        this.outletsService = outletsService;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
    }

    @Override
    public JpaRepository<Educator, UUID> getRepository() {
        return educatorRepository;
    }

    public Educator create(Educator educator) {
        return educatorRepository.save(educator);
    }

    public Educator addInstitution(@NonNull UUID educatorId, @NonNull UUID institutionId) {
        Educator educator = findById(educatorId);
        Institution institution = institutionsService.findById(institutionId);
        educator.addInstitution(institution);
        return educatorRepository.save(educator);
    }

    public Educator addEducatorClientPersona(@NonNull UUID educatorId, @NonNull UUID educatorClientPersonaId) {
        Educator educator = findById(educatorId);
        EducatorClientPersona educatorClientPersona = educatorClientPersonaRepository.findById(educatorClientPersonaId).orElseThrow(() -> new ResourceNotFoundException("educator client persona not found"));
        educator.addEducatorClientPersona(educatorClientPersona);
        return educatorRepository.save(educator);
    }

    public Educator addOulets(UUID educatorId, List<UUID> outletIds) {
        Educator educator = findById(educatorId);
        List<Outlet> outlets = outletsService.findByIds(outletIds);
        educator.addOutlets(outlets);
        return educatorRepository.save(educator);
    }

}
