package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;

public interface CreateLessonPlanUseCase {
    LessonPlan createLessonPlan(LessonPlan plan, String lessonId);
}
