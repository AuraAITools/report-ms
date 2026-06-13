package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonGroupPlan;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonGroupPlanRepository {
    LessonGroupPlan save(LessonGroupPlan plan);

    @Nullable LessonGroupPlan findById(UUID id);

    @Nullable LessonGroupPlan findByLessonGroupId(String lessonGroupId);

    boolean existsByLessonGroupId(String lessonGroupId);

    List<LessonGroupPlan> findAllByIds(List<UUID> ids);

    Map<String, LessonGroupPlan> findAllByLessonGroupIds(List<String> lessonGroupIds);
}
