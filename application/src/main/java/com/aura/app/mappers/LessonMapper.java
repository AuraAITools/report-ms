package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonInput;
import com.aura.app.codegen.types.CreateStandaloneLessonInput;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.core.entities.lessons.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    @Mapping(target = "studentLessonRegistrations", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "outlet", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "recap", ignore = true)
    @Mapping(target = "lessonQuizzes", ignore = true)
    @Mapping(target = "lessonPostponementRequests", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Lesson toDomainEntity(CreateStandaloneLessonInput createStandaloneLessonInput);

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "levels", ignore = true)
    @Mapping(target = "educators", ignore = true)
    @Mapping(target = "students", ignore = true)
    LessonResponse toResponseDto(Lesson lesson);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "studentLessonRegistrations", ignore = true)
    @Mapping(target = "recap", ignore = true)
    @Mapping(target = "outlet", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonQuizzes", ignore = true)
    @Mapping(target = "lessonPostponementRequests", ignore = true)
    @Mapping(target = "lessonPlans", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "lessonCancellationRequests", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Lesson toDomainEntity(CreateLessonInput createLessonInput);
}
