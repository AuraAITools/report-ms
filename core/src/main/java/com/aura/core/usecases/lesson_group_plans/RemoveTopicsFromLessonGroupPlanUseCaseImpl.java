package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoveTopicsFromLessonGroupPlanUseCaseImpl implements RemoveTopicsFromLessonGroupPlanUseCase {

    private final LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;

    @Override
    public void removeTopicsFromLessonGroupPlan(String lessonGroupPlanId, List<String> topicIds) {
        if (topicIds.isEmpty()) {
            return;
        }
        lessonGroupPlanTopicLinkRepository.deleteAllByLessonGroupPlanIdAndTopicIdIn(lessonGroupPlanId, topicIds);
    }
}
