package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicSubjectLinkMapper {
    TopicSubjectLinkMapper INSTANCE = Mappers.getMapper(TopicSubjectLinkMapper.class);

    @Mapping(source = "topicId", target = "topic", qualifiedByName = "topicRef")
    @Mapping(source = "subjectId", target = "subject", qualifiedByName = "subjectRef")
    TopicSubjectLinkModel toModelEntity(TopicSubjectLink topicSubjectLink);

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "subject.id", target = "subjectId")
    TopicSubjectLink toDomain(TopicSubjectLinkModel topicSubjectLinkModel);

    @Named("topicRef")
    default TopicModel topicRef(String topicId) {
        if (topicId == null) {
            return null;
        }
        return TopicModel.builder().id(UUID.fromString(topicId)).build();
    }

    @Named("subjectRef")
    default SubjectModel subjectRef(String subjectId) {
        if (subjectId == null) {
            return null;
        }
        return SubjectModel.builder().id(UUID.fromString(subjectId)).build();
    }
}
