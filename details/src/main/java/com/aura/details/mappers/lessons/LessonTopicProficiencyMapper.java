package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.details.mappers.links.StudentLessonLinkMapper;
import com.aura.details.mappers.topics.TopicMapper;
import com.aura.details.tables.lessons.LessonTopicProficiencyTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TopicMapper.class, StudentLessonLinkMapper.class})
public interface LessonTopicProficiencyMapper {
    LessonTopicProficiencyMapper INSTANCE = Mappers.getMapper(LessonTopicProficiencyMapper.class);

    LessonTopicProficiency toDomainEntity(LessonTopicProficiencyTable lessonTopicProficiencyTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonTopicProficiencyTable toTableEntity(LessonTopicProficiency lessonTopicProficiency);
}

