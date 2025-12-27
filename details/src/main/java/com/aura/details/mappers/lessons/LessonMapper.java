package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.details.mappers.subjects.SubjectMapper;
import com.aura.details.tables.lessons.LessonTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SubjectMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    @Mapping(target = "lessonPostponementRequestTables", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "lessonQuizzTables", ignore = true)
    Lesson toDomainEntity(LessonTable lessonTable);

    @Mapping(target = "lessonPostponementRequestTables", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "lessonQuizzTables", ignore = true)
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonTable toTableEntity(Lesson lesson);
}

