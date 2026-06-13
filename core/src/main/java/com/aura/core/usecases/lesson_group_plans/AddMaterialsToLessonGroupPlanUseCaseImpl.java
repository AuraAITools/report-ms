package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.MaterialRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddMaterialsToLessonGroupPlanUseCaseImpl implements AddMaterialsToLessonGroupPlanUseCase {
    private final LessonGroupPlanMaterialLinkRepository lessonGroupPlanMaterialLinkRepository;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final MaterialRepository materialRepository;

    @Override
    public List<LessonGroupPlanMaterialLink> addMaterialsToLessonGroupPlan(String lessonGroupPlanId, List<String> materialIds, String tenantId) {
        if (materialIds.isEmpty()) return emptyList();
        LessonGroupPlan plan = ofNullable(lessonGroupPlanRepository.findById(UUID.fromString(lessonGroupPlanId)))
                .orElseThrow(() -> new ResourceNotFoundException("Lesson group plan", lessonGroupPlanId));
        List<Material> materials = materialIds.stream()
                .map(id -> ofNullable(materialRepository.findById(id))
                        .orElseThrow(() -> new ResourceNotFoundException("Material", id)))
                .toList();
        List<LessonGroupPlanMaterialLink> links = materials.stream()
                .map(material -> LessonGroupPlanMaterialLink.builder()
                        .lessonGroupPlanId(plan.getId())
                        .materialId(material.getId())
                        .tenantId(tenantId)
                        .build())
                .toList();
        return lessonGroupPlanMaterialLinkRepository.saveAll(links);
    }
}
