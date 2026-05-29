package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.LevelLessonLink;
import com.aura.core.repositories.LevelLessonLinkRepository;
import com.aura.details.springdata.mappers.links.LevelLessonLinkMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevelLessonLinkRepositoryImpl implements LevelLessonLinkRepository {
    private final JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public LevelLessonLink save(LevelLessonLink levelLessonLink) {
        LevelLessonLinkModel model = toAttachedModel(levelLessonLink);
        LevelLessonLinkModel saved = jpaLevelLessonLinkRepository.save(model);
        return LevelLessonLinkMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public List<LevelLessonLink> saveAll(List<LevelLessonLink> levelLessonLinks) {
        var modelEntities = levelLessonLinks.stream().map(this::toAttachedModel).toList();
        var createdModelEntities = jpaLevelLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(LevelLessonLinkMapper.INSTANCE::toDomain).toList();
    }

    private LevelLessonLinkModel toAttachedModel(LevelLessonLink link) {
        LevelLessonLinkModel model = LevelLessonLinkMapper.INSTANCE.toModelEntity(link);
        model.setLevel(Refs.ref(entityManager, LevelModel.class, link.getLevelId()));
        model.setLesson(Refs.ref(entityManager, LessonModel.class, link.getLessonId()));
        return model;
    }
}
