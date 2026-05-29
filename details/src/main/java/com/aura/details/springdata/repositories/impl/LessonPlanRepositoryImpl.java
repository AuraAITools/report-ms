package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.details.springdata.mappers.lessons.LessonPlanMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonPlanRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonPlanRepositoryImpl implements LessonPlanRepository {
    private final JpaLessonPlanRepository jpaLessonPlanRepository;
    private final EntityManager entityManager;

    @Override
    public LessonPlan save(LessonPlan plan) {
        LessonPlanModel model = LessonPlanMapper.INSTANCE.toModelEntity(plan);
        model.setLesson(Refs.ref(entityManager, LessonModel.class, plan.getLessonId()));
        LessonPlanModel saved = jpaLessonPlanRepository.save(model);
        return LessonPlanMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable LessonPlan findById(UUID id) {
        return jpaLessonPlanRepository.findById(id)
                .map(LessonPlanMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<LessonPlan> findAllByLessonId(String lessonId) {
        return jpaLessonPlanRepository.findAllByLessonId(UUID.fromString(lessonId)).stream()
                .map(LessonPlanMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<LessonPlan> findAllByIds(List<UUID> ids) {
        return jpaLessonPlanRepository.findAllById(ids).stream()
                .map(LessonPlanMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<LessonPlan>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<LessonPlan>> result = new HashMap<>();
        for (LessonPlanModel model : jpaLessonPlanRepository.findAllByLessonIdIn(uuids)) {
            String lessonId = model.getLesson().getId().toString();
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(LessonPlanMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }
}
