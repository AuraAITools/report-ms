package com.aura.core.usecases.schools;

import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateSchoolUseCaseImpl implements CreateSchoolUseCase {
    private final SchoolRepository schoolRepository;

    @Override
    public School createSchool(School school) {
        return schoolRepository.save(school);
    }
}
