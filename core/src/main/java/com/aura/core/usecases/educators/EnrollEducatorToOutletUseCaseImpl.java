package com.aura.core.usecases.educators;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.EducatorRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class EnrollEducatorToOutletUseCaseImpl implements EnrollEducatorToOutletUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public Outlet enroll(String educatorId, String outletId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createOutletEducatorLink(edu.getId(), outletId, edu.getTenantId()))
                .orElseThrow(() -> new TechnicalException("educator not found"));
    }
}
