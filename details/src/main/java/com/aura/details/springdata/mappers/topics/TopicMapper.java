package com.aura.details.springdata.mappers.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.tables.topics.TopicTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(target = "materialTopicLinks", ignore = true)
    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    @Mapping(target = "lessonPlanTopicLinks", ignore = true)
    Topic toDomainEntity(TopicTable topicTable);

    @Mapping(target = "materialTopicLinks", ignore = true)
    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    @Mapping(target = "lessonPlanTopicLinks", ignore = true)
    TopicTable toTableEntity(Topic topic);
}

