package com.aura.core.repositories;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;

public interface LessonGroupPlanTopicLinkRepository {
    List<LessonGroupPlanTopicLink> saveAll(List<LessonGroupPlanTopicLink> links);

    Map<String, List<Topic>> findTopicsByLessonGroupPlanIds(List<String> lessonGroupPlanIds);
}
