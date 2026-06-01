package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonGroupMapper {
    LessonGroupMapper INSTANCE = Mappers.getMapper(LessonGroupMapper.class);

    @Mapping(source = "lesson.id", target = "lessonId")
    LessonGroup toDomainEntity(LessonGroupModel model);

    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "lessonGroupPlan", ignore = true)
    @Mapping(target = "registeredStudents", ignore = true)
    LessonGroupModel toModelEntity(LessonGroup lessonGroup);
}
