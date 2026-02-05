package com.aura.core.repositories;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import java.util.List;
import java.util.UUID;

public interface SubjectRepository {
    Subject save(Subject subject);

    List<TopicSubjectLink> link(UUID topicId, List<UUID> ids, String tenantId);
}
