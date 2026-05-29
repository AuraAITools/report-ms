package com.aura.core.usecases.subjects;

import com.aura.core.entities.subjects.SubjectWithTopics;
import java.util.List;
import java.util.UUID;

public interface CreateTopicSubjectLinksUseCase {
    SubjectWithTopics linkTopicsToSubject(UUID subjectId, List<UUID> topicIds, String tenantId);
}
