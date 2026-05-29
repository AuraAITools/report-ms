package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentCourseLink;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentCourseLinkMapper {

    StudentCourseLinkMapper INSTANCE = Mappers.getMapper(StudentCourseLinkMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    StudentCourseLink toDomain(StudentCourseLinkModel model);

    // student and course are attached separately by the repository impl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    StudentCourseLinkModel toTable(StudentCourseLink link);
}
