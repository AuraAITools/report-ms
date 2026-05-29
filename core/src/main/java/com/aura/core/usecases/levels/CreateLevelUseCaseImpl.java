package com.aura.core.usecases.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateLevelUseCaseImpl implements CreateLevelUseCase {
    private final LevelRepository levelRepository;

    @Override
    public Level createLevel(Level level) {
        return levelRepository.save(level);
    }
}
