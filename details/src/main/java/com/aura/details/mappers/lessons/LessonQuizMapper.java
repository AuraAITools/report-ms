package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.mappers.lessons.LessonMapper;
import com.aura.details.tables.lessons.LessonQuizTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonQuizMapper {
    LessonQuizMapper INSTANCE = Mappers.getMapper(LessonQuizMapper.class);

    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    LessonQuiz toDomainEntity(LessonQuizTable lessonQuizTable);

    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonQuizTable toTableEntity(LessonQuiz lessonQuiz);
}

