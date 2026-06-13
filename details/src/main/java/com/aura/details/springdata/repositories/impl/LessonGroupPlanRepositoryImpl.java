package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.details.springdata.mappers.lessons.LessonGroupPlanMapper;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonGroupPlanRepository;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import static java.util.Collections.emptyMap;

@RequiredArgsConstructor
public class LessonGroupPlanRepositoryImpl implements LessonGroupPlanRepository {
    private final JpaLessonGroupPlanRepository jpaLessonGroupPlanRepository;
    private final EntityManager entityManager;

    @Override
    public LessonGroupPlan save(LessonGroupPlan plan) {
        LessonGroupPlanModel model = LessonGroupPlanMapper.INSTANCE.toModelEntity(plan);
        model.setLessonGroup(Refs.ref(entityManager, LessonGroupModel.class, plan.getLessonGroupId()));
        LessonGroupPlanModel saved = jpaLessonGroupPlanRepository.save(model);
        return LessonGroupPlanMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonGroupPlan findById(UUID id) {
        return jpaLessonGroupPlanRepository.findById(id)
                .map(LessonGroupPlanMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public @Nullable LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
        return jpaLessonGroupPlanRepository.findByLessonGroupId(UUID.fromString(lessonGroupId))
                .map(LessonGroupPlanMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public boolean existsByLessonGroupId(String lessonGroupId) {
        return jpaLessonGroupPlanRepository.existsByLessonGroupId(UUID.fromString(lessonGroupId));
    }

    @Override
    public List<LessonGroupPlan> findAllByIds(List<UUID> ids) {
        return jpaLessonGroupPlanRepository.findAllById(ids).stream()
                .map(LessonGroupPlanMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, LessonGroupPlan> findAllByLessonGroupIds(List<String> lessonGroupIds) {
        if (lessonGroupIds.isEmpty()) return emptyMap();
        List<UUID> uuids = lessonGroupIds.stream().map(UUID::fromString).toList();
        Map<String, LessonGroupPlan> result = new HashMap<>();
        for (LessonGroupPlanModel model : jpaLessonGroupPlanRepository.findAllByLessonGroupIdIn(uuids)) {
            String groupId = model.getLessonGroup().getId().toString();
            result.put(groupId, LessonGroupPlanMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }
}
