package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectCourseLinkMapper {

    SubjectCourseLinkMapper INSTANCE = Mappers.getMapper(SubjectCourseLinkMapper.class);

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "course.id", target = "courseId")
    SubjectCourseLink toDomain(SubjectCourseLinkModel model);

    // subject and course are attached separately by the repository impl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "course", ignore = true)
    SubjectCourseLinkModel toTable(SubjectCourseLink link);
}
