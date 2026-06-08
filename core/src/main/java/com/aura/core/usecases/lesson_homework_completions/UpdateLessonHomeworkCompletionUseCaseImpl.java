package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonHomeworkCompletionUseCaseImpl implements UpdateLessonHomeworkCompletionUseCase {
    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;

    @Override
    public @Nullable LessonHomeworkCompletion update(String id, Integer completion) {
        LessonHomeworkCompletion existing = lessonHomeworkCompletionRepository.findById(UUID.fromString(id));
        if (existing == null) {
            return null;
        }
        existing.setCompletion(completion);
        return lessonHomeworkCompletionRepository.save(existing);
    }
}
