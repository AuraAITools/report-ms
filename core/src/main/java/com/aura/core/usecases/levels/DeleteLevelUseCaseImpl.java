package com.aura.core.usecases.levels;

import com.aura.core.repositories.LevelRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLevelUseCaseImpl implements DeleteLevelUseCase {
    private final LevelRepository levelRepository;

    @Override
    public void deleteById(String id) {
        levelRepository.deleteById(id);
    }
}
