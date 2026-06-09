package com.aura.core.usecases.subjects;

import com.aura.core.entities.delete_impact.DeleteImpact;

public interface GetSubjectDeleteImpactUseCase {
    DeleteImpact getDeleteImpact(String subjectId);
}
