package com.aura.core.usecases.students;

import com.aura.core.entities.levels.Level;

public interface TagStudentToLevelUseCase {
    Level tagStudentToLevel(String studentId, String id);
}
