package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonPlanTopicLinkMapper {

    LessonPlanTopicLinkMapper INSTANCE = Mappers.getMapper(LessonPlanTopicLinkMapper.class);

    @Mapping(source = "lessonPlan.id", target = "lessonPlanId")
    @Mapping(source = "topic.id", target = "topicId")
    LessonPlanTopicLink toDomain(LessonPlanTopicLinkModel model);

    // lessonPlan + topic are attached separately by LessonPlanTopicLinkRepositoryImpl via
    // Refs.ref(entityManager, ...), because builder-stub *Model entities (id set, @Version null)
    // trip Hibernate's isTransient check during persist.
    @Mapping(target = "lessonPlan", ignore = true)
    @Mapping(target = "topic", ignore = true)
    LessonPlanTopicLinkModel toTable(LessonPlanTopicLink link);
}
