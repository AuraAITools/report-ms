package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonPlanMapper {
    LessonPlanMapper INSTANCE = Mappers.getMapper(LessonPlanMapper.class);

    @Mapping(source = "lesson.id", target = "lessonId")
    LessonPlan toDomainEntity(LessonPlanModel model);

    // lesson is attached separately by LessonPlanRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub LessonModel (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "lesson", ignore = true)
    LessonPlanModel toModelEntity(LessonPlan lessonPlan);
}
