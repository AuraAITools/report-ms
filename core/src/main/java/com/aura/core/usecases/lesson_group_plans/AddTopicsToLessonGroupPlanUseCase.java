package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import java.util.List;

public interface AddTopicsToLessonGroupPlanUseCase {
    List<LessonGroupPlanTopicLink> addTopicsToLessonGroupPlan(String lessonGroupPlanId, List<String> topicIds, String tenantId);
}
