package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import com.aura.core.repositories.LessonObjectiveTopicLinkRepository;
import com.aura.details.springdata.mappers.links.LessonObjectiveTopicLinkMapper;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonObjectiveTopicLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LessonObjectiveTopicLinkRepositoryImpl implements LessonObjectiveTopicLinkRepository {

    private final JpaLessonObjectiveTopicLinkRepository jpaLessonObjectiveTopicLinkRepository;
    private final EntityManager entityManager;

    @Override
    public List<LessonObjectiveTopicLink> saveAll(List<LessonObjectiveTopicLink> links) {
        List<LessonObjectiveTopicLinkModel> models = links.stream()
                .map(this::toAttachedModel)
                .toList();
        List<LessonObjectiveTopicLinkModel> saved = jpaLessonObjectiveTopicLinkRepository.saveAll(models);
        return saved.stream()
                .map(LessonObjectiveTopicLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    private LessonObjectiveTopicLinkModel toAttachedModel(LessonObjectiveTopicLink link) {
        LessonObjectiveTopicLinkModel model = LessonObjectiveTopicLinkMapper.INSTANCE.toModelEntity(link);
        model.setTopic(Refs.ref(entityManager, TopicModel.class, link.getTopicId()));
        model.setLessonObjective(Refs.ref(entityManager, LessonObjectiveModel.class, link.getLessonObjectiveId()));
        return model;
    }
}
