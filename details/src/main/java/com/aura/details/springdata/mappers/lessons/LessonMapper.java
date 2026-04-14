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

    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "lessonPostponementRequests", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "lessonQuizzes", ignore = true)
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "studentLessonRegistrations", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "outlet", ignore = true)
    Lesson toDomainEntity(LessonTable lessonTable);


    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "lessonPostponementRequests", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "lessonQuizzes", ignore = true)
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "studentLessonRegistrations", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "course", ignore = true)
    LessonTable toTableEntity(Lesson lesson);
}

