package com.aura.core.usecases.lessons;

import com.aura.core.entities.links.TopicLessonLink;
import java.util.List;

public interface AddTopicsToLessonUseCase {
    List<TopicLessonLink> addTopicsToLesson(String lessonId, List<String> topicIds, String tenantId);
}
