package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.repositories.LessonGroupPlanRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonGroupPlanUseCaseImpl implements UpdateLessonGroupPlanUseCase {

    private final LessonGroupPlanRepository lessonGroupPlanRepository;

    @Override
    public @Nullable LessonGroupPlan updateLessonGroupPlan(LessonGroupPlan lessonGroupPlan) {
        LessonGroupPlan existing = lessonGroupPlanRepository.findById(UUID.fromString(lessonGroupPlan.getId()));
        if (existing == null) {
            return null;
        }
        if (lessonGroupPlan.getPlan() != null) {
            existing.setPlan(lessonGroupPlan.getPlan());
        }
        if (lessonGroupPlan.getRecap() != null) {
            existing.setRecap(lessonGroupPlan.getRecap());
        }
        if (lessonGroupPlan.getState() != null) {
            existing.setState(lessonGroupPlan.getState());
        }
        return lessonGroupPlanRepository.save(existing);
    }
}
