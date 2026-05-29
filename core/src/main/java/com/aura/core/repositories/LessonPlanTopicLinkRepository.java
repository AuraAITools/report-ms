package com.aura.core.repositories;

import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;

public interface LessonPlanTopicLinkRepository {
    List<LessonPlanTopicLink> saveAll(List<LessonPlanTopicLink> links);

    Map<String, List<Topic>> findTopicsByLessonPlanIds(List<String> planIds);
}
