package com.aura.core.usecases.educators;

import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.EducatorRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteEducatorUseCaseImpl implements DeleteEducatorUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(educatorRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Educator", id));
        educatorRepository.deleteById(id);
    }
}
