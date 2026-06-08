package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import org.jspecify.annotations.Nullable;

public interface UpdateLessonHomeworkCompletionUseCase {
    @Nullable LessonHomeworkCompletion update(String id, Integer completion);
}
