package com.aura.core.usecases.materials;

import com.aura.core.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteMaterialUseCaseImpl implements DeleteMaterialUseCase {
    private final MaterialRepository materialRepository;

    @Override
    public void deleteById(String id) {
        materialRepository.deleteById(id);
    }
}
