package com.aura.details.services;

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

    public Subject createSubject(Subject subject) {
        return createSubjectUseCase.createSubject(subject);
    }

    @Transactional
    public List<TopicSubjectLink> createTopicSubjectLinks(UUID subjectId, List<UUID> topicIds, String tenantId) {
        return createTopicSubjectLinksUseCase.linkTopicsToSubject(subjectId, topicIds, tenantId);
    }
}
