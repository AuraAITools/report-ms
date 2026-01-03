package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.tables.lessons.LessonTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {SubjectMapper.class, OutletMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    @Mapping(target = "lessonPostponementRequests", source = "lessonPostponementRequestTables")
    @Mapping(target = "lessonQuizzes", source = "lessonQuizzTables")
    Lesson toDomainEntity(LessonTable lessonTable);

    @Mapping(target = "lessonPostponementRequestTables", source = "lessonPostponementRequests")
    @Mapping(target = "lessonQuizzTables", source = "lessonQuizzes")
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "studentLessonRegistrations", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "Id", source = "id")
    LessonTable toTableEntity(Lesson lesson);
}

