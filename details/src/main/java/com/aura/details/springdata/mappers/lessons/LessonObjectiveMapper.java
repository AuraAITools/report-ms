package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.springdata.tables.lessons.LessonObjectiveTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonObjectiveMapper {
    LessonObjectiveMapper INSTANCE = Mappers.getMapper(LessonObjectiveMapper.class);

    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    LessonObjective toDomainEntity(LessonObjectiveTable lessonObjectiveTable);

    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    @Mapping(target = "Id", source = "id")
    LessonObjectiveTable toTableEntity(LessonObjective lessonObjective);
}

