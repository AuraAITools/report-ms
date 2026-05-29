package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.repositories.LessonObjectiveRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateLessonObjectiveUseCaseImpl implements CreateLessonObjectiveUseCase {

    private final LessonObjectiveRepository lessonObjectiveRepository;

    @Override
    public LessonObjective createLessonObjective(LessonObjective lessonObjective) {
        return lessonObjectiveRepository.save(lessonObjective);
    }
}
