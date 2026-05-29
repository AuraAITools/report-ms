package com.aura.details.springdata.mappers.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.models.topics.TopicModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    Topic toDomainEntity(TopicModel topicModel);

    TopicModel toModelEntity(Topic topic);
}
