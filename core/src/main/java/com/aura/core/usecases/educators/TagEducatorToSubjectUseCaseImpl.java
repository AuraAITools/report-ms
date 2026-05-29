package com.aura.core.usecases.educators;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.EducatorRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class TagEducatorToSubjectUseCaseImpl implements TagEducatorToSubjectUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public Subject tag(String educatorId, String subjectId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createSubjectEducatorLink(educatorId, subjectId, edu.getTenantId()))
                .orElseThrow(() -> new TechnicalException("educator not found"));
    }
}
