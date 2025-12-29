package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPostponementRequest;
import com.aura.details.springdata.tables.lessons.LessonPostponementRequestTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonPostponementRequestMapper {
    LessonPostponementRequestMapper INSTANCE = Mappers.getMapper(LessonPostponementRequestMapper.class);

    LessonPostponementRequest toDomainEntity(LessonPostponementRequestTable lessonPostponementRequestTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonPostponementRequestTable toTableEntity(LessonPostponementRequest lessonPostponementRequest);
}

