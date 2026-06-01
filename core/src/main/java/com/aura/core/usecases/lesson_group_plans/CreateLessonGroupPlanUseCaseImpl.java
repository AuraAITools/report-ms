package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateLessonGroupPlanUseCaseImpl implements CreateLessonGroupPlanUseCase {
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final LessonGroupRepository lessonGroupRepository;

    @Override
    public LessonGroupPlan createLessonGroupPlan(LessonGroupPlan plan, String lessonGroupId) {
        LessonGroup group = ofNullable(lessonGroupRepository.findById(UUID.fromString(lessonGroupId)))
                .orElseThrow(() -> new RuntimeException("lesson group not found"));

        plan.setLessonGroupId(lessonGroupId);
        plan.setTenantId(group.getTenantId());
        plan.setState(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.UNPLANNED);
        return lessonGroupPlanRepository.save(plan);
    }
}
