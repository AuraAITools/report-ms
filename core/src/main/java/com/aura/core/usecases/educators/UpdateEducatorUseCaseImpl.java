package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.educators.Educator.EMPLOYMENT_TYPE;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.EducatorRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateEducatorUseCaseImpl implements UpdateEducatorUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public Educator update(String id,
                           @Nullable String name,
                           @Nullable LocalDate startDate,
                           @Nullable LocalDate dateOfBirth,
                           @Nullable String email,
                           @Nullable EMPLOYMENT_TYPE employmentType) {
        Educator existing = Optional.ofNullable(educatorRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Educator", id));
        if (name != null) existing.setName(name);
        if (startDate != null) existing.setStartDate(startDate);
        if (dateOfBirth != null) existing.setDateOfBirth(dateOfBirth);
        if (email != null) existing.setEmail(email);
        if (employmentType != null) existing.setEmploymentType(employmentType);
        return educatorRepository.save(existing);
    }
}
