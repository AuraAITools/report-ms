package com.aura.core.usecases.educators;

import com.aura.core.entities.subjects.Subject;

public interface TagEducatorToSubjectUseCase {
    Subject tag(String educatorId, String subjectId);
}
