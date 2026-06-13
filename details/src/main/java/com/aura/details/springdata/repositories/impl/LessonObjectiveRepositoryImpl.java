package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.repositories.LessonObjectiveRepository;
import com.aura.details.springdata.mappers.lessons.LessonObjectiveMapper;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonObjectiveRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonObjectiveTopicLinkRepository;
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
public class LessonObjectiveRepositoryImpl implements LessonObjectiveRepository {

    private final JpaLessonObjectiveRepository jpaLessonObjectiveRepository;
    private final JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository;
    private final EntityManager entityManager;

    @Override
    public LessonObjective save(LessonObjective lessonObjective) {
        LessonObjectiveModel toCreate = LessonObjectiveMapper.INSTANCE.toModelEntity(lessonObjective);
        toCreate.setLessonGroupPlan(Refs.ref(entityManager, LessonGroupPlanModel.class, lessonObjective.getLessonGroupPlanId()));
        LessonObjectiveModel created = jpaLessonObjectiveRepository.save(toCreate);
        return LessonObjectiveMapper.INSTANCE.toDomainEntity(created);
    }

    @Override
    public @Nullable LessonObjective findById(String id) {
        return jpaLessonObjectiveRepository.findById(UUID.fromString(id))
                .map(LessonObjectiveMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<LessonObjective> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return jpaLessonObjectiveRepository.findAllByLessonGroupPlanId(UUID.fromString(lessonGroupPlanId)).stream()
                .map(LessonObjectiveMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<LessonObjective>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
        if (lessonGroupPlanIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonGroupPlanIds.stream().map(UUID::fromString).toList();
        Map<String, List<LessonObjective>> result = new HashMap<>();
        for (Object[] row : jpaLessonObjectiveRepository.findObjectivesByLessonGroupPlanIds(uuids)) {
            String planId = row[0].toString();
            LessonObjectiveModel model = (LessonObjectiveModel) row[1];
            result.computeIfAbsent(planId, k -> new ArrayList<>())
                    .add(LessonObjectiveMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }

    @Override
    public List<LessonObjective> findAllByTenantId(String tenantId) {
        return jpaLessonObjectiveRepository.findAllByTenantId(tenantId).stream()
                .map(LessonObjectiveMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<LessonObjective>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<LessonObjective>> result = new HashMap<>();
        for (Object[] row : jpaLessonObjectiveRepository.findObjectivesByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            LessonObjectiveModel model = (LessonObjectiveModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(LessonObjectiveMapper.INSTANCE.toDomainEntity(model));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaLessonObjectiveRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllLessonObjectiveTopicLinksByLessonObjectiveId(String lessonObjectiveId) {
        jpaLessonObjectiveTopicLinkRepository.deleteAllByLessonObjectiveId(UUID.fromString(lessonObjectiveId));
        // Force flush so a same-transaction re-insert of any (lesson_objective_id, topic_id)
        // pair doesn't collide with the queued delete against uk_lesson_objective_topic.
        jpaLessonObjectiveTopicLinkRepository.flush();
    }
}
