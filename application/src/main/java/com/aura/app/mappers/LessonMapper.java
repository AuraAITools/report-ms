package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonInCourseInput;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.core.entities.lessons.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "levels", ignore = true)
    @Mapping(target = "educators", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "outlet", ignore = true)
    @Mapping(target = "outletRoom", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "lessonGroups", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonResponse toResponseDto(Lesson lesson);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "outletId", ignore = true)
    @Mapping(target = "lessonOutletRoomBooking", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "courseId", source = "courseId")
    @Mapping(source = "institutionId", target = "tenantId")
    Lesson toDomainEntity(CreateLessonInCourseInput createLessonInCourseInput);
}
