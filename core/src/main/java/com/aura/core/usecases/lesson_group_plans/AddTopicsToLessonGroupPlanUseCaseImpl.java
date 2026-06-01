package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddTopicsToLessonGroupPlanUseCaseImpl implements AddTopicsToLessonGroupPlanUseCase {
    private final LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final TopicRepository topicRepository;

    @Override
    public List<LessonGroupPlanTopicLink> addTopicsToLessonGroupPlan(String lessonGroupPlanId, List<String> topicIds, String tenantId) {
        if (topicIds.isEmpty()) return emptyList();
        LessonGroupPlan plan = ofNullable(lessonGroupPlanRepository.findById(UUID.fromString(lessonGroupPlanId)))
                .orElseThrow(() -> new RuntimeException("no lesson group plan id found"));
        List<Topic> topics = topicRepository.findAllByIds(topicIds.stream().map(UUID::fromString).toList());
        List<LessonGroupPlanTopicLink> links = topics.stream()
                .map(topic -> LessonGroupPlanTopicLink
                        .builder()
                        .topicId(topic.getId())
                        .lessonGroupPlanId(plan.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        return lessonGroupPlanTopicLinkRepository.saveAll(links);
    }
}
