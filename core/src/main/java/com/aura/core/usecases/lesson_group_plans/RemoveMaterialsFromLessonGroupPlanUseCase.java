package com.aura.core.usecases.lesson_group_plans;

import java.util.List;

public interface RemoveMaterialsFromLessonGroupPlanUseCase {
    void removeMaterialsFromLessonGroupPlan(String lessonGroupPlanId, List<String> materialIds);
}
