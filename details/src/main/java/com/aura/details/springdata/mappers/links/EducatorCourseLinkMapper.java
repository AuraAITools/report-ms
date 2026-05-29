package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.EducatorCourseLink;
import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducatorCourseLinkMapper {

    EducatorCourseLinkMapper INSTANCE = Mappers.getMapper(EducatorCourseLinkMapper.class);

    @Mapping(source = "educator.id", target = "educatorId")
    @Mapping(source = "course.id", target = "courseId")
    EducatorCourseLink toDomain(EducatorCourseLinkModel model);

    // educator and course are attached separately by the repository impl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "educator", ignore = true)
    @Mapping(target = "course", ignore = true)
    EducatorCourseLinkModel toTable(EducatorCourseLink link);
}
