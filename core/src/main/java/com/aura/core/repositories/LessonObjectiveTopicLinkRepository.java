package com.aura.core.repositories;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import java.util.List;

public interface LessonObjectiveTopicLinkRepository {
    List<LessonObjectiveTopicLink> saveAll(List<LessonObjectiveTopicLink> links);
}
