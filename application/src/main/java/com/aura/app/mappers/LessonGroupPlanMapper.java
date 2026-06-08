package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonGroupPlanForLessonGroupInput;
import com.aura.app.codegen.types.CreateLessonGroupPlanInLessonGroupInput;
import com.aura.app.codegen.types.CreateLessonObjectiveInLessonGroupPlanInput;
import com.aura.app.codegen.types.LessonGroupPlanResponse;
import com.aura.app.codegen.types.UpdateLessonGroupPlanInput;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonObjective;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonGroupPlanMapper {

    LessonGroupPlanMapper INSTANCE = Mappers.getMapper(LessonGroupPlanMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "recap", ignore = true)
    @Mapping(target = "state", ignore = true)
    LessonGroupPlan toDomainEntity(CreateLessonGroupPlanInLessonGroupInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonGroupId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "recap", ignore = true)
    @Mapping(target = "state", ignore = true)
    LessonGroupPlan toDomainEntity(CreateLessonGroupPlanForLessonGroupInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonGroupPlanId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonObjective toDomainEntity(CreateLessonObjectiveInLessonGroupPlanInput input);

    @Mapping(target = "lessonGroup", ignore = true)
    @Mapping(target = "topics", ignore = true)
    @Mapping(target = "lessonObjectives", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonGroupPlanResponse toDto(LessonGroupPlan plan);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonGroupId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonGroupPlan toDomainEntity(UpdateLessonGroupPlanInput input);
}
