package com.aura.core.usecases.students;

import com.aura.core.entities.students.Student;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface StudentQueryUseCase {
    @Nullable Student findById(String id);

    List<Student> findAllByTenantId(String tenantId);
}
