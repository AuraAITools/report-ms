package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import org.jspecify.annotations.Nullable;

public interface UpdateLessonGroupPlanUseCase {
    @Nullable LessonGroupPlan updateLessonGroupPlan(LessonGroupPlan lessonGroupPlan);
}
