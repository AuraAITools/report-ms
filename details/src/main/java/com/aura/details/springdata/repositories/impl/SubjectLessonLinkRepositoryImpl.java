package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.core.repositories.SubjectLessonLinkRepository;
import com.aura.details.springdata.mappers.links.SubjectLessonLinkMapper;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectLessonLinkRepositoryImpl implements SubjectLessonLinkRepository {
    private final JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public SubjectLessonLink save(SubjectLessonLink subjectLessonLink) {
        SubjectLessonLinkModel model = toAttachedModel(subjectLessonLink);
        SubjectLessonLinkModel saved = jpaSubjectLessonLinkRepository.save(model);
        return SubjectLessonLinkMapper.INSTANCE.toDomain(saved);
    }

    @Override
    public List<SubjectLessonLink> saveAll(List<SubjectLessonLink> subjectLessonLinks) {
        var modelEntities = subjectLessonLinks.stream().map(this::toAttachedModel).toList();
        var createdModelEntities = jpaSubjectLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(SubjectLessonLinkMapper.INSTANCE::toDomain).toList();
    }

    private SubjectLessonLinkModel toAttachedModel(SubjectLessonLink link) {
        SubjectLessonLinkModel model = SubjectLessonLinkMapper.INSTANCE.toModelEntity(link);
        model.setSubject(Refs.ref(entityManager, SubjectModel.class, link.getSubjectId()));
        model.setLesson(Refs.ref(entityManager, LessonModel.class, link.getLessonId()));
        return model;
    }
}
