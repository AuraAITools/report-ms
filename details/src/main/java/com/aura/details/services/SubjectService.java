package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectService {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Subject createSubject(Subject subject) {
        Subject created = createSubjectUseCase.createSubject(subject);
        relationshipService.writeRelationship(
                "subject", created.getId(), "parent_institution", "institution", created.getTenantId());
        return created;
    }

    @Transactional
    public List<TopicSubjectLink> createTopicSubjectLinks(UUID subjectId, List<UUID> topicIds, String tenantId) {
        return createTopicSubjectLinksUseCase.linkTopicsToSubject(subjectId, topicIds, tenantId);
    }
}
