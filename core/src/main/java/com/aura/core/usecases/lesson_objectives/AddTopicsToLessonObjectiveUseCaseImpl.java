package com.aura.core.usecases.lesson_objectives;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonObjectiveTopicLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class AddTopicsToLessonObjectiveUseCaseImpl implements AddTopicsToLessonObjectiveUseCase {
    private final LessonObjectiveTopicLinkRepository lessonObjectiveTopicLinkRepository;
    private final TopicRepository topicRepository;

    @Override
    public List<LessonObjectiveTopicLink> addTopicsToLessonObjective(String lessonObjectiveId,
                                                                     List<String> topicIds,
                                                                     String tenantId) {
        if (topicIds.isEmpty()) return emptyList();
        List<Topic> topics = topicRepository.findAllByIds(topicIds.stream().map(UUID::fromString).toList());
        List<LessonObjectiveTopicLink> links = topics.stream()
                .map(topic -> LessonObjectiveTopicLink.builder()
                        .topicId(topic.getId())
                        .lessonObjectiveId(lessonObjectiveId)
                        .tenantId(tenantId)
                        .build())
                .toList();
        return lessonObjectiveTopicLinkRepository.saveAll(links);
    }
}
