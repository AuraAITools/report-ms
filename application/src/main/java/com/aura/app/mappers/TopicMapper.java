package com.aura.app.mappers;

import com.aura.app.codegen.types.TopicInput;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.core.entities.topics.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { DateToEpochMapper.class })
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "topicSubjectLinks", ignore = true)
    @Mapping(target = "materialTopicLinks", ignore = true)
    @Mapping(target = "lessonPlanTopicLinks", ignore = true)
    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Topic toDomainEntity(TopicInput topicInput, String tenantId);

    TopicResponse toDto(Topic topic);
}
