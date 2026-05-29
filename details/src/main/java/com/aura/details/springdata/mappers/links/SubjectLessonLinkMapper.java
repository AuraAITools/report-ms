package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectLessonLinkMapper {
    SubjectLessonLinkMapper INSTANCE = Mappers.getMapper(SubjectLessonLinkMapper.class);

    // subject and lesson are attached separately by SubjectLessonLinkRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    SubjectLessonLinkModel toModelEntity(SubjectLessonLink domain);

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "lesson.id", target = "lessonId")
    SubjectLessonLink toDomain(SubjectLessonLinkModel table);
}
