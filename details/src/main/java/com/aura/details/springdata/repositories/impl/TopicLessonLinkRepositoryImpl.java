package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.TopicLessonLink;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.details.springdata.mappers.links.TopicLessonLinkMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicLessonLinkRepositoryImpl implements TopicLessonLinkRepository {
    private final JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public TopicLessonLink save(TopicLessonLink topicLessonLink) {
        TopicLessonLinkModel model = toAttachedModel(topicLessonLink);
        TopicLessonLinkModel saved = jpaTopicLessonLinkRepository.save(model);
        return TopicLessonLinkMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public List<TopicLessonLink> saveAll(List<TopicLessonLink> topicLessonLinks) {
        var modelEntities = topicLessonLinks.stream().map(this::toAttachedModel).toList();
        var createdModelEntities = jpaTopicLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(TopicLessonLinkMapper.INSTANCE::toDomain).toList();
    }

    @Override
    public Set<String> findTopicIdsByLessonId(String lessonId) {
        return jpaTopicLessonLinkRepository.findTopicIdsByLessonId(UUID.fromString(lessonId)).stream()
                .map(UUID::toString)
                .collect(Collectors.toSet());
    }

    private TopicLessonLinkModel toAttachedModel(TopicLessonLink link) {
        TopicLessonLinkModel model = TopicLessonLinkMapper.INSTANCE.toModelEntity(link);
        model.setTopic(Refs.ref(entityManager, TopicModel.class, link.getTopicId()));
        model.setLesson(Refs.ref(entityManager, LessonModel.class, link.getLessonId()));
        return model;
    }
}
