package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonCancellationRequest;
import com.aura.details.springdata.tables.lessons.LessonCancellationRequestTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonCancellationRequestMapper {
    LessonCancellationRequestMapper INSTANCE = Mappers.getMapper(LessonCancellationRequestMapper.class);

    LessonCancellationRequest toDomainEntity(LessonCancellationRequestTable lessonCancellationRequestTable);

    @Mapping(target = "Id", source = "id")
    LessonCancellationRequestTable toTableEntity(LessonCancellationRequest lessonCancellationRequest);
}

