package com.aura.core.usecases.schools;

import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchoolQueryUseCaseImpl implements SchoolQueryUseCase {
    private final SchoolRepository schoolRepository;

    @Override
    public List<School> findAllByTenantId(String tenantId) {
        return schoolRepository.findAllByTenantId(tenantId);
    }
}
