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

    @Mapping(target = "Id", source = "id")
    LessonPostponementRequestTable toTableEntity(LessonPostponementRequest lessonPostponementRequest);
}

