package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.details.springdata.models.links.LessonGroupPlanTopicLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonGroupPlanTopicLinkMapper {

    LessonGroupPlanTopicLinkMapper INSTANCE = Mappers.getMapper(LessonGroupPlanTopicLinkMapper.class);

    @Mapping(source = "lessonGroupPlan.id", target = "lessonGroupPlanId")
    @Mapping(source = "topic.id", target = "topicId")
    LessonGroupPlanTopicLink toDomain(LessonGroupPlanTopicLinkModel model);

    // lessonGroupPlan + topic are attached separately by LessonGroupPlanTopicLinkRepositoryImpl via
    // Refs.ref(entityManager, ...), because builder-stub *Model entities (id set, @Version null)
    // trip Hibernate's isTransient check during persist.
    @Mapping(target = "lessonGroupPlan", ignore = true)
    @Mapping(target = "topic", ignore = true)
    LessonGroupPlanTopicLinkModel toTable(LessonGroupPlanTopicLink link);
}
