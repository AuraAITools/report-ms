package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.links.LessonPlanTopicLink;
import java.util.List;

public interface AddTopicsToLessonPlanUseCase {
    List<LessonPlanTopicLink> addTopicsToLessonPlan(String planId, List<String> topicIds, String tenantId);
}
