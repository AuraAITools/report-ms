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

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonCancellationRequestTable toTableEntity(LessonCancellationRequest lessonCancellationRequest);
}

