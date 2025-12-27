package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.mappers.lessons.LessonMapper;
import com.aura.details.tables.lessons.LessonObjectiveTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonObjectiveMapper {
    LessonObjectiveMapper INSTANCE = Mappers.getMapper(LessonObjectiveMapper.class);

    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    LessonObjective toDomainEntity(LessonObjectiveTable lessonObjectiveTable);

    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonObjectiveTable toTableEntity(LessonObjective lessonObjective);
}

