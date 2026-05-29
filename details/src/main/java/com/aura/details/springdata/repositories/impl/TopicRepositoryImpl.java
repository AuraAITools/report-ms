package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepository {

    private final JpaTopicRepository jpaTopicRepository;
    private final JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    private final JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository;

    @Override
    public Topic save(Topic topic) {
        TopicModel newTopicModel = TopicMapper.INSTANCE.toModelEntity(topic);
        TopicModel createdTopicModel = jpaTopicRepository.save(newTopicModel);
        return TopicMapper.INSTANCE.toDomainEntity(createdTopicModel);
    }

    @Override
    public @Nullable Topic findById(String id) {
        return jpaTopicRepository.findById(UUID.fromString(id))
                .map(TopicMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<Topic> findAllByIds(List<UUID> topicIds) {
        return jpaTopicRepository.findAllById(topicIds).stream().map(TopicMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<Topic> findAllByTenantId(String tenantId) {
        return jpaTopicRepository.findByTenantId(tenantId).stream()
                .map(TopicMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<Topic>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Topic>> result = new HashMap<>();
        for (Object[] row : jpaTopicLessonLinkRepository.findTopicsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            TopicModel topicModel = (TopicModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(TopicMapper.INSTANCE.toDomainEntity(topicModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaTopicRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllTopicSubjectLinksByTopicId(String topicId) {
        jpaTopicSubjectLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }
}
