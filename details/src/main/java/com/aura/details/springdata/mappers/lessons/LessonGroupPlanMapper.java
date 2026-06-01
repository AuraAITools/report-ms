package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonGroupPlanMapper {
    LessonGroupPlanMapper INSTANCE = Mappers.getMapper(LessonGroupPlanMapper.class);

    @Mapping(source = "lessonGroup.id", target = "lessonGroupId")
    LessonGroupPlan toDomainEntity(LessonGroupPlanModel model);

    // lessonGroup is attached separately by LessonGroupPlanRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub LessonGroupModel (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "lessonGroup", ignore = true)
    LessonGroupPlanModel toModelEntity(LessonGroupPlan plan);
}
