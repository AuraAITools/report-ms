package com.aura.core.usecases.lesson_group_plans;

import java.util.List;

public interface RemoveTopicsFromLessonGroupPlanUseCase {
    void removeTopicsFromLessonGroupPlan(String lessonGroupPlanId, List<String> topicIds);
}
