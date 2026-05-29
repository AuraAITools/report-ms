package com.aura.core.repositories;

import com.aura.core.entities.links.TopicLessonLink;
import java.util.List;
import java.util.Set;

public interface TopicLessonLinkRepository {
    TopicLessonLink save(TopicLessonLink topicLessonLink);

    List<TopicLessonLink> saveAll(List<TopicLessonLink> topicLessonLinks);

    Set<String> findTopicIdsByLessonId(String lessonId);
}
