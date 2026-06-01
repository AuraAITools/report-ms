package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.details.springdata.mappers.lessons.LessonGroupMapper;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        LessonGroupMapper.class
})
public interface StudentLessonLinkMapper {
    StudentLessonLinkMapper INSTANCE = Mappers.getMapper(StudentLessonLinkMapper.class);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    StudentLessonLink toDomainEntity(StudentLessonLinkModel studentLessonLinkModel);

    // student, lesson, and lessonGroup are attached separately by StudentLessonLinkRepositoryImpl via
    // Refs.ref(entityManager, ...), because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "lessonGroup", ignore = true)
    @Mapping(target = "studentLessonReport", ignore = true)
    StudentLessonLinkModel toModelEntity(StudentLessonLink studentLessonLink);
}
