package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.TopicLessonLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class AddTopicsToLessonUseCaseImpl implements AddTopicsToLessonUseCase {
    private final TopicLessonLinkRepository topicLessonLinkRepository;
    private final LessonRepository lessonRepository;
    private final TopicRepository topicRepository;

    @Override
    public List<TopicLessonLink> addTopicsToLesson(String lessonId, List<String> topicIds, String tenantId) {
        if (topicIds.isEmpty()) return emptyList();
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("no lesson id found"));
        List<Topic> topics = topicRepository.findAllByIds(topicIds.stream().map(UUID::fromString).toList());
        List<TopicLessonLink> topicLessonLinks = topics.stream()
                .map(topic -> TopicLessonLink
                        .builder()
                        .topicId(topic.getId())
                        .lessonId(lesson.getId())
                        .tenantId(tenantId)
                        .build()).toList();

        return topicLessonLinkRepository.saveAll(topicLessonLinks);
    }
}
