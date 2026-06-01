package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonObjective;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface LessonObjectiveRepository {
    LessonObjective save(LessonObjective lessonObjective);

    @Nullable LessonObjective findById(String id);

    List<LessonObjective> findAllByLessonGroupPlanId(String lessonGroupPlanId);

    Map<String, List<LessonObjective>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds);

    List<LessonObjective> findAllByTenantId(String tenantId);

    Map<String, List<LessonObjective>> findAllByLessonIds(List<String> lessonIds);

    void deleteById(String id);

    void deleteAllLessonObjectiveTopicLinksByLessonObjectiveId(String lessonObjectiveId);
}
