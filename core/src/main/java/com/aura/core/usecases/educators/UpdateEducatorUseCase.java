package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.educators.Educator.EMPLOYMENT_TYPE;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

public interface UpdateEducatorUseCase {
    Educator update(String id,
                    @Nullable String name,
                    @Nullable LocalDate startDate,
                    @Nullable LocalDate dateOfBirth,
                    @Nullable String email,
                    @Nullable EMPLOYMENT_TYPE employmentType);
}
