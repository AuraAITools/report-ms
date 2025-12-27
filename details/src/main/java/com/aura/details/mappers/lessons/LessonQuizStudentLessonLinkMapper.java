package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuizStudentLessonLink;
import com.aura.details.mappers.lessons.LessonQuizMapper;
import com.aura.details.tables.lessons.LessonQuizStudentLessonLinkTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonQuizMapper.class})
public interface LessonQuizStudentLessonLinkMapper {
    LessonQuizStudentLessonLinkMapper INSTANCE = Mappers.getMapper(LessonQuizStudentLessonLinkMapper.class);

    LessonQuizStudentLessonLink toDomainEntity(LessonQuizStudentLessonLinkTable lessonQuizStudentLessonLinkTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonQuizStudentLessonLinkTable toTableEntity(LessonQuizStudentLessonLink lessonQuizStudentLessonLink);
}

