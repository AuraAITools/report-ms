package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonPlan;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonPlanRepository {
    LessonPlan save(LessonPlan lessonPlan);

    @Nullable LessonPlan findById(UUID id);

    List<LessonPlan> findAllByLessonId(String lessonId);

    List<LessonPlan> findAllByIds(List<UUID> ids);

    Map<String, List<LessonPlan>> findAllByLessonIds(List<String> lessonIds);
}
