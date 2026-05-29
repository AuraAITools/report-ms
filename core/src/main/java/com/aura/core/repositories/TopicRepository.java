package com.aura.core.repositories;

import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface TopicRepository {
    Topic save(Topic topic);

    @Nullable Topic findById(String id);

    List<Topic> findAllByIds(List<UUID> topicIds);

    List<Topic> findAllByTenantId(String tenantId);

    Map<String, List<Topic>> findAllByLessonIds(List<String> lessonIds);

    void deleteById(String id);

    void deleteAllTopicSubjectLinksByTopicId(String topicId);
}
