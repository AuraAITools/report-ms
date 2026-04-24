package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EducatorMapper.class, LessonMapper.class})
public interface EducatorLessonLinkMapper {
    EducatorLessonLinkMapper INSTANCE = Mappers.getMapper(EducatorLessonLinkMapper.class);

    EducatorLessonLinkModel toModelEntity(EducatorLessonLink domain);

    EducatorLessonLink toDomain(EducatorLessonLinkModel table);
}
