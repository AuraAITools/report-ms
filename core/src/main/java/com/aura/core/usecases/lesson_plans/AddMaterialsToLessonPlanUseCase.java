package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import java.util.List;

public interface AddMaterialsToLessonPlanUseCase {
    List<LessonPlanMaterialLink> addMaterialsToLessonPlan(String planId, List<String> materialIds, String tenantId);
}
