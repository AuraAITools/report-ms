package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonObjectiveInLessonPlanInput;
import com.aura.app.codegen.types.CreateLessonPlanForLessonInput;
import com.aura.app.codegen.types.CreateLessonPlanInLessonInput;
import com.aura.app.codegen.types.LessonPlanResponse;
import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.lessons.LessonPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface LessonPlanMapper {

    LessonPlanMapper INSTANCE = Mappers.getMapper(LessonPlanMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonPlan toDomainEntity(CreateLessonPlanInLessonInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonPlan toDomainEntity(CreateLessonPlanForLessonInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonPlanId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonObjective toDomainEntity(CreateLessonObjectiveInLessonPlanInput input);

    // ignore lesson + topics — provided by @BatchMapping resolvers
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "topics", ignore = true)
    LessonPlanResponse toDto(LessonPlan plan);
}
