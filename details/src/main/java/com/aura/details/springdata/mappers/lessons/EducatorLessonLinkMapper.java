package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducatorLessonLinkMapper {
    EducatorLessonLinkMapper INSTANCE = Mappers.getMapper(EducatorLessonLinkMapper.class);

    // educator and lesson are attached separately by EducatorLessonLinkRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "educator", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    EducatorLessonLinkModel toModelEntity(EducatorLessonLink domain);

    @Mapping(source = "educator.id", target = "educatorId")
    @Mapping(source = "lesson.id", target = "lessonId")
    EducatorLessonLink toDomain(EducatorLessonLinkModel table);
}
