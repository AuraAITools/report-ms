package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateStandaloneLessonInput;
import com.aura.app.codegen.types.LessonResponse;
import com.aura.core.entities.lessons.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

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
    Lesson toDomainEntity(CreateStandaloneLessonInput createStandaloneLessonInput, String tenantId);

    LessonResponse toResponseDto(Lesson lesson);
}
