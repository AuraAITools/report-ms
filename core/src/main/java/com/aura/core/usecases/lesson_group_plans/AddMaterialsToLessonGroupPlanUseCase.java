package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import java.util.List;

public interface AddMaterialsToLessonGroupPlanUseCase {
    List<LessonGroupPlanMaterialLink> addMaterialsToLessonGroupPlan(String lessonGroupPlanId, List<String> materialIds, String tenantId);
}
