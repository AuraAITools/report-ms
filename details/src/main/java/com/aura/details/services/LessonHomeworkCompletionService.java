package com.aura.details.services;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.usecases.lesson_homework_completions.DeleteLessonHomeworkCompletionUseCase;
import com.aura.core.usecases.lesson_homework_completions.LessonHomeworkCompletionQueryUseCase;
import com.aura.core.usecases.lesson_homework_completions.UpdateLessonHomeworkCompletionUseCase;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonHomeworkCompletionService {

    private final LessonHomeworkCompletionQueryUseCase queryUseCase;
    private final UpdateLessonHomeworkCompletionUseCase updateUseCase;
    private final DeleteLessonHomeworkCompletionUseCase deleteUseCase;

    public @Nullable LessonHomeworkCompletion findById(UUID id) {
        return queryUseCase.findById(id);
    }

    @Transactional
    public @Nullable LessonHomeworkCompletion update(String id, Integer completion) {
        return updateUseCase.update(id, completion);
    }

    @Transactional
    public void deleteById(UUID id) {
        deleteUseCase.deleteById(id);
    }
}
