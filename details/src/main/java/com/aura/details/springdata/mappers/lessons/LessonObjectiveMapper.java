package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonObjectiveMapper {
    LessonObjectiveMapper INSTANCE = Mappers.getMapper(LessonObjectiveMapper.class);

    @Mapping(source = "lessonGroupPlan.id", target = "lessonGroupPlanId")
    LessonObjective toDomainEntity(LessonObjectiveModel model);

    // lessonGroupPlan is attached separately by LessonObjectiveRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "lessonGroupPlan", ignore = true)
    @Mapping(target = "lessonObjectiveTopicLinks", ignore = true)
    LessonObjectiveModel toModelEntity(LessonObjective lessonObjective);
}
