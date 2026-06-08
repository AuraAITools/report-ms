package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoveMaterialsFromLessonGroupPlanUseCaseImpl implements RemoveMaterialsFromLessonGroupPlanUseCase {

    private final LessonGroupPlanMaterialLinkRepository lessonGroupPlanMaterialLinkRepository;

    @Override
    public void removeMaterialsFromLessonGroupPlan(String lessonGroupPlanId, List<String> materialIds) {
        if (materialIds.isEmpty()) {
            return;
        }
        lessonGroupPlanMaterialLinkRepository.deleteAllByLessonGroupPlanIdAndMaterialIdIn(lessonGroupPlanId, materialIds);
    }
}
