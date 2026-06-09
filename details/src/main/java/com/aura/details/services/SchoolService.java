package com.aura.details.services;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.usecases.schools.CreateSchoolUseCase;
import com.aura.core.usecases.schools.DeleteSchoolUseCase;
import com.aura.core.usecases.schools.GetSchoolDeleteImpactUseCase;
import com.aura.core.usecases.schools.SchoolQueryUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SchoolService {
    private final SchoolQueryUseCase schoolQueryUseCase;
    private final CreateSchoolUseCase createSchoolUseCase;
    private final DeleteSchoolUseCase deleteSchoolUseCase;
    private final GetSchoolDeleteImpactUseCase getSchoolDeleteImpactUseCase;
    private final SchoolRepository schoolRepository;

    public List<School> findAllByTenantId(String tenantId) {
        return schoolQueryUseCase.findAllByTenantId(tenantId);
    }

    public @Nullable School findById(String id) {
        return schoolRepository.findById(id);
    }

    public DeleteImpact getDeleteImpact(String id) {
        return getSchoolDeleteImpactUseCase.getDeleteImpact(id);
    }

    @Transactional
    public School createSchool(School school) {
        return createSchoolUseCase.createSchool(school);
    }

    @Transactional
    public void deleteById(String id) {
        schoolRepository.nullOutSchoolOnStudentsBySchoolId(id);
        deleteSchoolUseCase.deleteById(id);
    }
}
