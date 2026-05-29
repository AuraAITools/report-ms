package com.aura.core.usecases.students;

import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.StudentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteStudentUseCaseImpl implements DeleteStudentUseCase {
    private final StudentRepository studentRepository;

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(studentRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        studentRepository.deleteById(id);
    }
}
