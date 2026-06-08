package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonHomeworkCompletionQueryUseCase {
    @Nullable LessonHomeworkCompletion findById(UUID id);
}
