package com.aura.core.usecases.lesson_objectives;

import com.aura.core.repositories.LessonObjectiveRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonObjectiveUseCaseImpl implements DeleteLessonObjectiveUseCase {

    private final LessonObjectiveRepository lessonObjectiveRepository;

    @Override
    public void deleteById(String id) {
        lessonObjectiveRepository.deleteById(id);
    }
}
