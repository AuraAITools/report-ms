package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonHomeworkCompletionQueryUseCaseImpl implements LessonHomeworkCompletionQueryUseCase {
    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;

    @Override
    public @Nullable LessonHomeworkCompletion findById(UUID id) {
        return lessonHomeworkCompletionRepository.findById(id);
    }
}
