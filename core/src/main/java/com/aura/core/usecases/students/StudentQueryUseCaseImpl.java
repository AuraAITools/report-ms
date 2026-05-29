package com.aura.core.usecases.students;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentQueryUseCaseImpl implements StudentQueryUseCase {
    private final StudentRepository studentRepository;

    @Override
    public @Nullable Student findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> findAllByTenantId(String tenantId) {
        return studentRepository.findAllByTenantId(tenantId);
    }
}
