package com.aura.app.mappers;

import com.aura.app.codegen.types.LessonHomeworkCompletionResponse;
import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonHomeworkCompletionMapper {

    LessonHomeworkCompletionMapper INSTANCE = Mappers.getMapper(LessonHomeworkCompletionMapper.class);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonHomeworkCompletionResponse toDto(LessonHomeworkCompletion completion);
}

