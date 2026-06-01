package com.aura.app.mappers;

import com.aura.app.codegen.types.TopicInput;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.core.entities.topics.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { DateToEpochMapper.class, AccountMinimalDetailMapper.class })
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Topic toDomainEntity(TopicInput topicInput);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    TopicResponse toDto(Topic topic);
}
