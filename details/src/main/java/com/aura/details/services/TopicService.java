package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class TopicService {

    private final CreateTopicUseCase createTopicUseCase;
    private final TopicQueryUseCase topicQueryUseCase;
    private final DeleteTopicUseCase deleteTopicUseCase;
    private final TopicRepository topicRepository;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Topic createTopic(Topic topic) {
        Topic created = createTopicUseCase.createTopic(topic);
        relationshipService.writeRelationship(
                "topic", created.getId(), "parent", "institution", created.getTenantId());
        return created;
    }

    public @Nullable Topic findById(String id) {
        return topicQueryUseCase.findById(id);
    }

    public List<Topic> findAllByTenantId(String tenantId) {
        return topicQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public void deleteById(String id) {
        topicRepository.deleteAllTopicSubjectLinksByTopicId(id);
        deleteTopicUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("topic", id);
        relationshipService.deleteAllRelationshipsForResource("topic_user_group", id);
    }
}
