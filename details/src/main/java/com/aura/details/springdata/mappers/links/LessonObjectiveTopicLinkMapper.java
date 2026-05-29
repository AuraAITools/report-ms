package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonObjectiveTopicLinkMapper {
    LessonObjectiveTopicLinkMapper INSTANCE = Mappers.getMapper(LessonObjectiveTopicLinkMapper.class);

    // topic and lessonObjective are attached separately by LessonObjectiveTopicLinkRepositoryImpl
    // via Refs.ref(entityManager, ...), because a builder-stub *Model (id set, @Version null) trips
    // Hibernate's isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "lessonObjective", ignore = true)
    LessonObjectiveTopicLinkModel toModelEntity(LessonObjectiveTopicLink domain);

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "lessonObjective.id", target = "lessonObjectiveId")
    LessonObjectiveTopicLink toDomain(LessonObjectiveTopicLinkModel table);
}
