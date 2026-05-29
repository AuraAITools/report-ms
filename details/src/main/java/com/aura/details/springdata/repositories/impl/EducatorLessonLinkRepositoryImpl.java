package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.details.springdata.mappers.lessons.EducatorLessonLinkMapper;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EducatorLessonLinkRepositoryImpl implements EducatorLessonLinkRepository {
    private final JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public EducatorLessonLink save(EducatorLessonLink educatorLessonLink) {
        EducatorLessonLinkModel model = toAttachedModel(educatorLessonLink);
        EducatorLessonLinkModel saved = jpaEducatorLessonLinkRepository.save(model);
        return EducatorLessonLinkMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public List<EducatorLessonLink> saveAll(List<EducatorLessonLink> educatorLessonLinks) {
        var modelEntities = educatorLessonLinks.stream().map(this::toAttachedModel).toList();
        var createdModelEntities = jpaEducatorLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(EducatorLessonLinkMapper.INSTANCE::toDomain).toList();
    }

    private EducatorLessonLinkModel toAttachedModel(EducatorLessonLink link) {
        EducatorLessonLinkModel model = EducatorLessonLinkMapper.INSTANCE.toModelEntity(link);
        model.setEducator(Refs.ref(entityManager, EducatorModel.class, link.getEducatorId()));
        model.setLesson(Refs.ref(entityManager, LessonModel.class, link.getLessonId()));
        return model;
    }
}
