package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.EducatorRepository;


import static java.util.Optional.ofNullable;

public class TagEducatorToLevelUseCaseImpl implements TagEducatorToLevelUseCase {
    private final EducatorRepository educatorRepository;

    public TagEducatorToLevelUseCaseImpl(EducatorRepository educatorRepository) {
        this.educatorRepository = educatorRepository;
    }

    @Override
    public Level tag(String educatorId, String levelId) {
        Educator educator = ofNullable(educatorRepository.findById(educatorId)).orElseThrow(() -> new RuntimeException("no educator found"));
        return educatorRepository.createLevelEducatorLink(educatorId, levelId, educator.getTenantId());
    }
}
