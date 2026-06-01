package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;

public interface CreateLessonGroupPlanUseCase {
    LessonGroupPlan createLessonGroupPlan(LessonGroupPlan plan, String lessonGroupId);
}
