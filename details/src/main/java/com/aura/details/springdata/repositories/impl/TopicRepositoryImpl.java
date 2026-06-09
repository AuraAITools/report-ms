package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.delete_impact.AffectedResourceGroup;
import com.aura.core.entities.delete_impact.CleanupAction;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonObjectiveTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaMaterialTopicLinkRepository;
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
    private final JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository;
    private final JpaMaterialTopicLinkRepository jpaMaterialTopicLinkRepository;
    private final JpaLessonGroupPlanTopicLinkRepository jpaLessonGroupPlanTopicLinkRepository;

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
    public DeleteImpact findDeleteImpact(String topicId) {
        UUID id = UUID.fromString(topicId);
        List<AffectedResourceGroup> groups = new ArrayList<>();
        groups.add(buildGroup("Subject", jpaTopicSubjectLinkRepository.findSubjectIdsByTopicId(id)));
        groups.add(buildGroup("LessonObjective", jpaLessonObjectiveTopicLinkRepository.findLessonObjectiveIdsByTopicId(id)));
        groups.add(buildGroup("Material", jpaMaterialTopicLinkRepository.findMaterialIdsByTopicId(id)));
        groups.add(buildGroup("Lesson", jpaTopicLessonLinkRepository.findLessonIdsByTopicId(id)));
        groups.add(buildGroup("LessonGroupPlan", jpaLessonGroupPlanTopicLinkRepository.findLessonGroupPlanIdsByTopicId(id)));
        return DeleteImpact.builder()
                .resourceType("Topic")
                .resourceId(topicId)
                .affectedGroups(groups)
                .build();
    }

    @Override
    public void deleteAllTopicSubjectLinksByTopicId(String topicId) {
        jpaTopicSubjectLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }

    @Override
    public void deleteAllLessonObjectiveTopicLinksByTopicId(String topicId) {
        jpaLessonObjectiveTopicLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }

    @Override
    public void deleteAllMaterialTopicLinksByTopicId(String topicId) {
        jpaMaterialTopicLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }

    @Override
    public void deleteAllTopicLessonLinksByTopicId(String topicId) {
        jpaTopicLessonLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }

    @Override
    public void deleteAllLessonGroupPlanTopicLinksByTopicId(String topicId) {
        jpaLessonGroupPlanTopicLinkRepository.deleteAllByTopicId(UUID.fromString(topicId));
    }

    private AffectedResourceGroup buildGroup(String resourceType, List<UUID> ids) {
        return AffectedResourceGroup.builder()
                .resourceType(resourceType)
                .cleanupAction(CleanupAction.DELETE)
                .affectedIds(ids.stream().map(UUID::toString).toList())
                .build();
    }
}
