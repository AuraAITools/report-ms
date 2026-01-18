package com.aura.core.usecases.educators;

import com.aura.core.entities.levels.Level;

public interface TagEducatorToLevelUseCase {
    Level tag(String educatorId, String levelId);
}
