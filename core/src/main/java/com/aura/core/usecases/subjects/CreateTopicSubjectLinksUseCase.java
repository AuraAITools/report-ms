package com.aura.core.usecases.subjects;

import com.aura.core.entities.links.TopicSubjectLink;
import java.util.List;
import java.util.UUID;

public interface CreateTopicSubjectLinksUseCase {
    List<TopicSubjectLink> linkTopicsToSubject(UUID subjectId, List<UUID> topicIds, String tenantId);
}
