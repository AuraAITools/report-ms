package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.repositories.LessonObjectiveRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonObjectiveQueryUseCaseImpl implements LessonObjectiveQueryUseCase {
    private final LessonObjectiveRepository lessonObjectiveRepository;

    @Override
    public @Nullable LessonObjective findById(String id) {
        return lessonObjectiveRepository.findById(id);
    }

    @Override
    public List<LessonObjective> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return lessonObjectiveRepository.findAllByLessonGroupPlanId(lessonGroupPlanId);
    }

    @Override
    public List<LessonObjective> findAllByTenantId(String tenantId) {
        return lessonObjectiveRepository.findAllByTenantId(tenantId);
    }
}
