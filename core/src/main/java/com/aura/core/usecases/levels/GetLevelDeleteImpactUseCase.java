package com.aura.core.usecases.levels;

import com.aura.core.entities.delete_impact.DeleteImpact;

public interface GetLevelDeleteImpactUseCase {
    DeleteImpact getDeleteImpact(String levelId);
}
