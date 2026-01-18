package com.aura.core.usecases.educators;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.EducatorRepository;


import static java.util.Optional.ofNullable;

public class EnrollEducatorToOutletUseCaseImpl implements EnrollEducatorToOutletUseCase {
    private final EducatorRepository educatorRepository;

    public EnrollEducatorToOutletUseCaseImpl(EducatorRepository educatorRepository) {
        this.educatorRepository = educatorRepository;
    }

    @Override
    public Outlet enroll(String educatorId, String outletId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createOutletEducatorLink(edu.getId(), outletId, edu.getTenantId()))
                .orElseThrow(() -> new RuntimeException("educator not found"));
    }
}
