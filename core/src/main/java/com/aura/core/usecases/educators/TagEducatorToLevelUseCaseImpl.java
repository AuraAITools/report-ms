package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.levels.Level;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.EducatorRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class TagEducatorToLevelUseCaseImpl implements TagEducatorToLevelUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public Level tag(String educatorId, String levelId) {
        Educator educator = ofNullable(educatorRepository.findById(educatorId)).orElseThrow(() -> new TechnicalException("no educator found"));
        return educatorRepository.createLevelEducatorLink(educatorId, levelId, educator.getTenantId());
    }
}
