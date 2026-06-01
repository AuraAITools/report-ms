package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LessonObjectiveQueryUseCase {
    @Nullable LessonObjective findById(String id);

    List<LessonObjective> findAllByLessonGroupPlanId(String lessonGroupPlanId);

    List<LessonObjective> findAllByTenantId(String tenantId);
}
