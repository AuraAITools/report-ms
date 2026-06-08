package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonHomeworkCompletionRepository {
    LessonHomeworkCompletion save(LessonHomeworkCompletion lessonHomeworkCompletion);

    @Nullable LessonHomeworkCompletion findById(UUID id);

    void deleteById(UUID id);
}
