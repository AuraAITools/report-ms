package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import java.util.List;

public interface AddTopicsToLessonObjectiveUseCase {
    List<LessonObjectiveTopicLink> addTopicsToLessonObjective(String lessonObjectiveId,
                                                              List<String> topicIds,
                                                              String tenantId);
}
