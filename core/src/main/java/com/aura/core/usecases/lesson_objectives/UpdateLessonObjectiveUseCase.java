package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import org.jspecify.annotations.Nullable;

public interface UpdateLessonObjectiveUseCase {
    @Nullable LessonObjective updateLessonObjective(LessonObjective lessonObjective);
}
