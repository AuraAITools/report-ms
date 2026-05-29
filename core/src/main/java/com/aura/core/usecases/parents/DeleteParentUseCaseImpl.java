package com.aura.core.usecases.parents;

import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.ParentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteParentUseCaseImpl implements DeleteParentUseCase {
    private final ParentRepository parentRepository;

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(parentRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Parent", id));
        parentRepository.deleteById(id);
    }
}
