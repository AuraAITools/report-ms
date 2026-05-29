package com.aura.core.usecases.schools;

import com.aura.core.repositories.SchoolRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteSchoolUseCaseImpl implements DeleteSchoolUseCase {
    private final SchoolRepository schoolRepository;

    @Override
    public void deleteById(String id) {
        schoolRepository.deleteById(id);
    }
}
