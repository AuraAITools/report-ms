package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import org.jspecify.annotations.Nullable;

public interface CreateLessonHomeworkCompletionUseCase {
    LessonHomeworkCompletion create(String studentLessonReportId, @Nullable Integer completion, String tenantId);
}
