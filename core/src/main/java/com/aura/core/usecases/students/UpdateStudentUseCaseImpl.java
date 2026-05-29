package com.aura.core.usecases.students;

import com.aura.core.entities.students.Student;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.StudentRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateStudentUseCaseImpl implements UpdateStudentUseCase {
    private final StudentRepository studentRepository;

    @Override
    public Student update(String id,
                          @Nullable String name,
                          @Nullable String email,
                          @Nullable LocalDate dateOfBirth) {
        Student existing = Optional.ofNullable(studentRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        if (name != null) existing.setName(name);
        if (email != null) existing.setEmail(email);
        if (dateOfBirth != null) existing.setDateOfBirth(dateOfBirth);
        return studentRepository.save(existing);
    }
}
