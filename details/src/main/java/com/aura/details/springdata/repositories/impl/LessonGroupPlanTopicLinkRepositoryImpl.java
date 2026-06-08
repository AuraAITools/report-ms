package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.details.springdata.mappers.links.LessonGroupPlanTopicLinkMapper;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.links.LessonGroupPlanTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanTopicLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonGroupPlanTopicLinkRepositoryImpl implements LessonGroupPlanTopicLinkRepository {
    private final JpaLessonGroupPlanTopicLinkRepository jpaLessonGroupPlanTopicLinkRepository;
    private final EntityManager entityManager;

    @Override
    public List<LessonGroupPlanTopicLink> saveAll(List<LessonGroupPlanTopicLink> links) {
        if (links.isEmpty()) return Collections.emptyList();
        List<LessonGroupPlanTopicLinkModel> modelEntities = links.stream()
                .map(link -> {
                    LessonGroupPlanTopicLinkModel m = LessonGroupPlanTopicLinkMapper.INSTANCE.toTable(link);
                    m.setLessonGroupPlan(Refs.ref(entityManager, LessonGroupPlanModel.class, link.getLessonGroupPlanId()));
                    m.setTopic(Refs.ref(entityManager, TopicModel.class, link.getTopicId()));
                    return m;
                })
                .toList();
        return jpaLessonGroupPlanTopicLinkRepository.saveAll(modelEntities).stream()
                .map(LessonGroupPlanTopicLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Map<String, List<Topic>> findTopicsByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
        if (lessonGroupPlanIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonGroupPlanIds.stream().map(UUID::fromString).toList();
        Map<String, List<Topic>> result = new HashMap<>();
        for (Object[] row : jpaLessonGroupPlanTopicLinkRepository.findTopicsByLessonGroupPlanIds(uuids)) {
            String planId = row[0].toString();
            TopicModel topicModel = (TopicModel) row[1];
            result.computeIfAbsent(planId, k -> new ArrayList<>())
                    .add(TopicMapper.INSTANCE.toDomainEntity(topicModel));
        }
        return result;
    }

    @Override
    public java.util.Set<String> findTopicIdsByLessonGroupPlanId(String lessonGroupPlanId) {
        return jpaLessonGroupPlanTopicLinkRepository.findTopicIdsByLessonGroupPlanId(UUID.fromString(lessonGroupPlanId))
                .stream()
                .map(UUID::toString)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public void deleteAllByLessonGroupPlanIdAndTopicIdIn(String lessonGroupPlanId, List<String> topicIds) {
        if (topicIds.isEmpty()) return;
        List<UUID> topicUuids = topicIds.stream().map(UUID::fromString).toList();
        jpaLessonGroupPlanTopicLinkRepository.deleteAllByLessonGroupPlanIdAndTopicIdIn(
                UUID.fromString(lessonGroupPlanId), topicUuids);
    }
}
