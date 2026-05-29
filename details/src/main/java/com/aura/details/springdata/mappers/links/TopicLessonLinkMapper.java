package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.TopicLessonLink;
import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicLessonLinkMapper {
    TopicLessonLinkMapper INSTANCE = Mappers.getMapper(TopicLessonLinkMapper.class);

    // topic and lesson are attached separately by TopicLessonLinkRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    TopicLessonLinkModel toModelEntity(TopicLessonLink domain);

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "lesson.id", target = "lessonId")
    TopicLessonLink toDomain(TopicLessonLinkModel table);
}
