package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonPlanTopicLinkRepository;
import com.aura.details.springdata.mappers.links.LessonPlanTopicLinkMapper;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonPlanTopicLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonPlanTopicLinkRepositoryImpl implements LessonPlanTopicLinkRepository {
    private final JpaLessonPlanTopicLinkRepository jpaLessonPlanTopicLinkRepository;
    private final EntityManager entityManager;

    @Override
    public List<LessonPlanTopicLink> saveAll(List<LessonPlanTopicLink> links) {
        if (links.isEmpty()) return Collections.emptyList();
        List<LessonPlanTopicLinkModel> modelEntities = links.stream()
                .map(link -> {
                    LessonPlanTopicLinkModel m = LessonPlanTopicLinkMapper.INSTANCE.toTable(link);
                    m.setLessonPlan(Refs.ref(entityManager, LessonPlanModel.class, link.getLessonPlanId()));
                    m.setTopic(Refs.ref(entityManager, TopicModel.class, link.getTopicId()));
                    return m;
                })
                .toList();
        return jpaLessonPlanTopicLinkRepository.saveAll(modelEntities).stream()
                .map(LessonPlanTopicLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Map<String, List<Topic>> findTopicsByLessonPlanIds(List<String> planIds) {
        if (planIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = planIds.stream().map(UUID::fromString).toList();
        Map<String, List<Topic>> result = new HashMap<>();
        for (Object[] row : jpaLessonPlanTopicLinkRepository.findTopicsByLessonPlanIds(uuids)) {
            String planId = row[0].toString();
            TopicModel topicModel = (TopicModel) row[1];
            result.computeIfAbsent(planId, k -> new ArrayList<>())
                    .add(TopicMapper.INSTANCE.toDomainEntity(topicModel));
        }
        return result;
    }
}
