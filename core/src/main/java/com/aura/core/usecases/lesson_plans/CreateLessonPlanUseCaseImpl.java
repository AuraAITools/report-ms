package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateLessonPlanUseCaseImpl implements CreateLessonPlanUseCase {
    private final LessonPlanRepository lessonPlanRepository;
    private final LessonRepository lessonRepository;

    @Override
    public LessonPlan createLessonPlan(LessonPlan plan, String lessonId) {
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("lesson not found"));

        plan.setLessonId(lessonId);
        plan.setTenantId(lesson.getTenantId());
        plan.setState(LessonPlan.LESSON_PLAN_STATE.UNPLANNED);
        return lessonPlanRepository.save(plan);
    }
}
