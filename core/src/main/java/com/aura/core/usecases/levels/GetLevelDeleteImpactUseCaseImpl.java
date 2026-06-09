package com.aura.core.usecases.levels;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.repositories.LevelRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetLevelDeleteImpactUseCaseImpl implements GetLevelDeleteImpactUseCase {
    private final LevelRepository levelRepository;

    @Override
    public DeleteImpact getDeleteImpact(String levelId) {
        return levelRepository.findDeleteImpact(levelId);
    }
}
