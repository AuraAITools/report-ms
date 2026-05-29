package com.aura.details.services;

import com.aura.core.entities.students.Student;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.students.DeleteStudentUseCase;
import com.aura.core.usecases.students.StudentQueryUseCase;
import com.aura.core.usecases.students.UpdateStudentUseCase;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentService {
    private final StudentQueryUseCase studentQueryUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final StudentRepository studentRepository;

    public @Nullable Student findById(String id) {
        return studentQueryUseCase.findById(id);
    }

    public List<Student> findAllByTenantId(String tenantId) {
        return studentQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Student updateById(String id,
                              @Nullable String name,
                              @Nullable String email,
                              @Nullable LocalDate dateOfBirth) {
        return updateStudentUseCase.update(id, name, email, dateOfBirth);
    }

    @Transactional
    public void deleteById(String id) {
        studentRepository.deleteAllStudentCourseLinksByStudentId(id);
        studentRepository.deleteAllStudentOutletLinksByStudentId(id);
        studentRepository.deleteAllStudentLessonLinksByStudentId(id);
        studentRepository.deleteAllSubjectStudentLinksByStudentId(id);
        studentRepository.deleteAllAccountStudentLinksByStudentId(id);
        deleteStudentUseCase.deleteById(id);
    }
}
