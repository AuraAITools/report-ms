package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.core.repositories.LessonPlanMaterialLinkRepository;
import com.aura.details.springdata.mappers.links.LessonPlanMaterialLinkMapper;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonPlanMaterialsLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonPlanMaterialLinkRepositoryImpl implements LessonPlanMaterialLinkRepository {
    private final JpaLessonPlanMaterialsLinkRepository jpaLessonPlanMaterialsLinkRepository;
    private final EntityManager entityManager;

    @Override
    public List<LessonPlanMaterialLink> saveAll(List<LessonPlanMaterialLink> links) {
        if (links.isEmpty()) return Collections.emptyList();
        List<LessonPlanMaterialsLinkModel> modelEntities = links.stream()
                .map(link -> {
                    LessonPlanMaterialsLinkModel m = LessonPlanMaterialLinkMapper.INSTANCE.toTable(link);
                    m.setLessonPlan(Refs.ref(entityManager, LessonPlanModel.class, link.getLessonPlanId()));
                    m.setMaterial(Refs.ref(entityManager, MaterialModel.class, link.getMaterialId()));
                    return m;
                })
                .toList();
        return jpaLessonPlanMaterialsLinkRepository.saveAll(modelEntities).stream()
                .map(LessonPlanMaterialLinkMapper.INSTANCE::toDomain)
                .toList();
    }
}
