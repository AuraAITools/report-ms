package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonPlanTopicLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddTopicsToLessonPlanUseCaseImpl implements AddTopicsToLessonPlanUseCase {
    private final LessonPlanTopicLinkRepository lessonPlanTopicLinkRepository;
    private final LessonPlanRepository lessonPlanRepository;
    private final TopicRepository topicRepository;

    @Override
    public List<LessonPlanTopicLink> addTopicsToLessonPlan(String planId, List<String> topicIds, String tenantId) {
        if (topicIds.isEmpty()) return emptyList();
        LessonPlan plan = ofNullable(lessonPlanRepository.findById(UUID.fromString(planId)))
                .orElseThrow(() -> new RuntimeException("no lesson plan id found"));
        List<Topic> topics = topicRepository.findAllByIds(topicIds.stream().map(UUID::fromString).toList());
        List<LessonPlanTopicLink> links = topics.stream()
                .map(topic -> LessonPlanTopicLink
                        .builder()
                        .topicId(topic.getId())
                        .lessonPlanId(plan.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        return lessonPlanTopicLinkRepository.saveAll(links);
    }
}
