package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.repositories.EducatorLessonLinkRepository;
import com.aura.details.springdata.mappers.lessons.EducatorLessonLinkMapper;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.tables.links.EducatorLessonLinkTable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EducatorLessonLinkRepositoryImpl implements EducatorLessonLinkRepository {
    private final JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;

    @Override
    public EducatorLessonLink save(EducatorLessonLink educatorLessonLink) {
        EducatorLessonLinkTable educatorLessonLinkTable = jpaEducatorLessonLinkRepository.save(EducatorLessonLinkMapper.INSTANCE.toTableEntity(educatorLessonLink));
        return EducatorLessonLinkMapper.INSTANCE.toDomain(educatorLessonLinkTable);
    }

    @Override
    public List<EducatorLessonLink> saveAll(List<EducatorLessonLink> educatorLessonLinks) {
        var tableEntities = educatorLessonLinks.stream().map(EducatorLessonLinkMapper.INSTANCE::toTableEntity).toList();
        var createdTableEntities = jpaEducatorLessonLinkRepository.saveAll(tableEntities);
        return createdTableEntities.stream().map(EducatorLessonLinkMapper.INSTANCE::toDomain).toList();
    }
}
