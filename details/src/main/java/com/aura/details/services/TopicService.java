package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.topics.Topic;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicService {

    private final CreateTopicUseCase createTopicUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Topic createTopic(Topic topic) {
        Topic created = createTopicUseCase.createTopic(topic);
        relationshipService.writeRelationship(
                "topic", created.getId(), "parent_institution", "institution", created.getTenantId());
        return created;
    }
}
