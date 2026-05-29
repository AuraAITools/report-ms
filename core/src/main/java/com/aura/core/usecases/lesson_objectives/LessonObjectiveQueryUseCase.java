package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.lessons.LessonObjective;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LessonObjectiveQueryUseCase {
    @Nullable LessonObjective findById(String id);

    List<LessonObjective> findAllByLessonPlanId(String lessonPlanId);

    List<LessonObjective> findAllByTenantId(String tenantId);
}
