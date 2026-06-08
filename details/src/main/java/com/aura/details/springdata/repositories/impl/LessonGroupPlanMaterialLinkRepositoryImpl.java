package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import com.aura.details.springdata.mappers.links.LessonGroupPlanMaterialLinkMapper;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.links.LessonGroupPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanMaterialsLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonGroupPlanMaterialLinkRepositoryImpl implements LessonGroupPlanMaterialLinkRepository {
    private final JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository;
    private final EntityManager entityManager;

    @Override
    public List<LessonGroupPlanMaterialLink> saveAll(List<LessonGroupPlanMaterialLink> links) {
        if (links.isEmpty()) return Collections.emptyList();
        List<LessonGroupPlanMaterialsLinkModel> modelEntities = links.stream()
                .map(link -> {
                    LessonGroupPlanMaterialsLinkModel m = LessonGroupPlanMaterialLinkMapper.INSTANCE.toTable(link);
                    m.setLessonGroupPlan(Refs.ref(entityManager, LessonGroupPlanModel.class, link.getLessonGroupPlanId()));
                    m.setMaterial(Refs.ref(entityManager, MaterialModel.class, link.getMaterialId()));
                    return m;
                })
                .toList();
        return jpaLessonGroupPlanMaterialsLinkRepository.saveAll(modelEntities).stream()
                .map(LessonGroupPlanMaterialLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public List<LessonGroupPlanMaterialLink> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return jpaLessonGroupPlanMaterialsLinkRepository.findAllByLessonGroupPlanId(java.util.UUID.fromString(lessonGroupPlanId)).stream()
                .map(LessonGroupPlanMaterialLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public void deleteAllByLessonGroupPlanIdAndMaterialIdIn(String lessonGroupPlanId, List<String> materialIds) {
        if (materialIds.isEmpty()) return;
        List<java.util.UUID> materialUuids = materialIds.stream().map(java.util.UUID::fromString).toList();
        jpaLessonGroupPlanMaterialsLinkRepository.deleteAllByLessonGroupPlanIdAndMaterialIdIn(
                java.util.UUID.fromString(lessonGroupPlanId), materialUuids);
    }
}
