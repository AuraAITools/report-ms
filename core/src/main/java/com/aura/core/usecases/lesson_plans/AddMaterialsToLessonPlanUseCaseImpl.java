package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.LessonPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.MaterialRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddMaterialsToLessonPlanUseCaseImpl implements AddMaterialsToLessonPlanUseCase {
    private final LessonPlanMaterialLinkRepository lessonPlanMaterialLinkRepository;
    private final LessonPlanRepository lessonPlanRepository;
    private final MaterialRepository materialRepository;

    @Override
    public List<LessonPlanMaterialLink> addMaterialsToLessonPlan(String planId, List<String> materialIds, String tenantId) {
        if (materialIds.isEmpty()) return emptyList();
        LessonPlan plan = ofNullable(lessonPlanRepository.findById(UUID.fromString(planId)))
                .orElseThrow(() -> new RuntimeException("no lesson plan id found"));
        List<Material> materials = materialIds.stream()
                .map(id -> ofNullable(materialRepository.findById(id))
                        .orElseThrow(() -> new RuntimeException("material id not found: " + id)))
                .toList();
        List<LessonPlanMaterialLink> links = materials.stream()
                .map(material -> LessonPlanMaterialLink.builder()
                        .lessonPlanId(plan.getId())
                        .materialId(material.getId())
                        .tenantId(tenantId)
                        .build())
                .toList();
        return lessonPlanMaterialLinkRepository.saveAll(links);
    }
}
