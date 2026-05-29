package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialTopicLinkMapper {
    MaterialTopicLinkMapper INSTANCE = Mappers.getMapper(MaterialTopicLinkMapper.class);

    @Mapping(source = "topicId", target = "topic", qualifiedByName = "topicRef")
    @Mapping(source = "materialId", target = "material", qualifiedByName = "materialRef")
    MaterialTopicLinkModel toModelEntity(MaterialTopicLink domain);

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "material.id", target = "materialId")
    MaterialTopicLink toDomain(MaterialTopicLinkModel table);

    @Named("topicRef")
    default TopicModel topicRef(String topicId) {
        if (topicId == null) {
            return null;
        }
        return TopicModel.builder().id(UUID.fromString(topicId)).build();
    }

    @Named("materialRef")
    default MaterialModel materialRef(String materialId) {
        if (materialId == null) {
            return null;
        }
        return MaterialModel.builder().id(UUID.fromString(materialId)).build();
    }
}
