package com.aura.core.usecases.subjects;

import com.aura.core.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteSubjectUseCaseImpl implements DeleteSubjectUseCase {
    private final SubjectRepository subjectRepository;

    @Override
    public void deleteById(String id) {
        subjectRepository.deleteById(id);
    }
}
