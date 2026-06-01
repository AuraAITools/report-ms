package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonObjectiveInput;
import com.aura.app.codegen.types.LessonObjectiveResponse;
import com.aura.app.codegen.types.UpdateLessonObjectiveInput;
import com.aura.core.entities.lessons.LessonObjective;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonObjectiveMapper {

    LessonObjectiveMapper INSTANCE = Mappers.getMapper(LessonObjectiveMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    LessonObjective toDomainEntity(CreateLessonObjectiveInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lessonGroupPlanId", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    LessonObjective toDomainEntity(UpdateLessonObjectiveInput input);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonObjectiveResponse toDto(LessonObjective lessonObjective);
}
