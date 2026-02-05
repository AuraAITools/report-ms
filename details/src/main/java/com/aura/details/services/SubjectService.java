package com.aura.details.services;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectService {
    private final CreateSubjectUseCase createSubjectUseCase;

    public Subject createSubject(Subject subject) {
        return createSubjectUseCase.createSubject(subject);
    }
}
