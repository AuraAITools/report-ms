package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.details.springdata.mappers.lessons.EducatorLessonLinkMapper;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EducatorLessonLinkRepositoryImpl implements EducatorLessonLinkRepository {
    private final JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;

    @Override
    public EducatorLessonLink save(EducatorLessonLink educatorLessonLink) {
        EducatorLessonLinkModel educatorLessonLinkModel = jpaEducatorLessonLinkRepository.save(EducatorLessonLinkMapper.INSTANCE.toModelEntity(educatorLessonLink));
        return EducatorLessonLinkMapper.INSTANCE.toDomain(educatorLessonLinkModel);
    }

    @Override
    public List<EducatorLessonLink> saveAll(List<EducatorLessonLink> educatorLessonLinks) {
        var modelEntities = educatorLessonLinks.stream().map(EducatorLessonLinkMapper.INSTANCE::toModelEntity).toList();
        var createdModelEntities = jpaEducatorLessonLinkRepository.saveAll(modelEntities);
        return createdModelEntities.stream().map(EducatorLessonLinkMapper.INSTANCE::toDomain).toList();
    }
}
