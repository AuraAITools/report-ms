package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonHomeworkCompletionUseCaseImpl implements DeleteLessonHomeworkCompletionUseCase {
    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;

    @Override
    public void deleteById(UUID id) {
        lessonHomeworkCompletionRepository.deleteById(id);
    }
}
