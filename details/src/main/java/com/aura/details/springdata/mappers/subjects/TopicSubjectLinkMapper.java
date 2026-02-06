package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.tables.links.TopicSubjectLinkTable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TopicMapper.class, SubjectMapper.class})
public interface TopicSubjectLinkMapper {
    TopicSubjectLinkMapper INSTANCE = Mappers.getMapper(TopicSubjectLinkMapper.class);

    TopicSubjectLinkTable toTableEntity(TopicSubjectLink topicSubjectLink);

    TopicSubjectLink toDomain(TopicSubjectLinkTable topicSubjectLinkTable);
}
