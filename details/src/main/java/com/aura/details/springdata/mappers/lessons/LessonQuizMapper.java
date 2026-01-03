package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.tables.lessons.LessonQuizTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonQuizMapper {
    LessonQuizMapper INSTANCE = Mappers.getMapper(LessonQuizMapper.class);

    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    LessonQuiz toDomainEntity(LessonQuizTable lessonQuizTable);

    @Mapping(target = "lessonQuizStudentLessonLinks", ignore = true)
    @Mapping(target = "Id", source = "id")
    LessonQuizTable toTableEntity(LessonQuiz lessonQuiz);
}

