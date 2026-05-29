package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.repositories.LessonObjectiveRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonObjectiveUseCaseImpl implements UpdateLessonObjectiveUseCase {

    private final LessonObjectiveRepository lessonObjectiveRepository;

    @Override
    public @Nullable LessonObjective updateLessonObjective(LessonObjective lessonObjective) {
        LessonObjective existing = lessonObjectiveRepository.findById(lessonObjective.getId());
        if (existing == null || !existing.getTenantId().equals(lessonObjective.getTenantId())) {
            return null;
        }
        existing.setName(lessonObjective.getName());
        existing.setObjective(lessonObjective.getObjective());
        return lessonObjectiveRepository.save(existing);
    }
}
