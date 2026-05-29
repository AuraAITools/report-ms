package com.aura.core.usecases.students;

import com.aura.core.entities.students.Student;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

public interface UpdateStudentUseCase {
    Student update(String id,
                   @Nullable String name,
                   @Nullable String email,
                   @Nullable LocalDate dateOfBirth);
}
