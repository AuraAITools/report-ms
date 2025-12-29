package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.details.springdata.mappers.links.StudentLessonLinkMapper;
import com.aura.details.springdata.tables.lessons.LessonHomeworkCompletionTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentLessonLinkMapper.class})
public interface LessonHomeworkCompletionMapper {
    LessonHomeworkCompletionMapper INSTANCE = Mappers.getMapper(LessonHomeworkCompletionMapper.class);

    LessonHomeworkCompletion toDomainEntity(LessonHomeworkCompletionTable lessonHomeworkCompletionTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonHomeworkCompletionTable toTableEntity(LessonHomeworkCompletion lessonHomeworkCompletion);
}

