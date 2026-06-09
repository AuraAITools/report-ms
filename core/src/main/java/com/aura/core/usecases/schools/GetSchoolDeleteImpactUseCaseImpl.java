package com.aura.core.usecases.schools;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.repositories.SchoolRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSchoolDeleteImpactUseCaseImpl implements GetSchoolDeleteImpactUseCase {
    private final SchoolRepository schoolRepository;

    @Override
    public DeleteImpact getDeleteImpact(String schoolId) {
        return schoolRepository.findDeleteImpact(schoolId);
    }
}
