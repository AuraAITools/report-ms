package com.aura.core.usecases.educators;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.EducatorRepository;


import static java.util.Optional.ofNullable;

public class TagEducatorToSubjectUseCaseImpl implements TagEducatorToSubjectUseCase {
    private final EducatorRepository educatorRepository;

    public TagEducatorToSubjectUseCaseImpl(EducatorRepository educatorRepository) {
        this.educatorRepository = educatorRepository;
    }

    @Override
    public Subject tag(String educatorId, String subjectId) {
        return ofNullable(educatorRepository.findById(educatorId))
                .map(edu -> educatorRepository.createSubjectEducatorLink(educatorId, subjectId, edu.getTenantId()))
                .orElseThrow(() -> new RuntimeException("educator not found"));
    }
}
