package com.aura.core.usecases.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateSubjectUseCaseImpl implements CreateSubjectUseCase {

    private final SubjectRepository subjectRepository;

    @Override
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
}
