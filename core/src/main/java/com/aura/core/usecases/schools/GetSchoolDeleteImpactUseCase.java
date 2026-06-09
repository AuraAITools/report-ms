package com.aura.core.usecases.schools;

import com.aura.core.entities.delete_impact.DeleteImpact;

public interface GetSchoolDeleteImpactUseCase {
    DeleteImpact getDeleteImpact(String schoolId);
}
