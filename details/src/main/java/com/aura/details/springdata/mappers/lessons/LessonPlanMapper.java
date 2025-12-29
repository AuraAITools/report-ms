package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.details.springdata.tables.lessons.LessonPlanTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {LessonMapper.class})
public interface LessonPlanMapper {
    LessonPlanMapper INSTANCE = Mappers.getMapper(LessonPlanMapper.class);

    @Mapping(target = "lessonPlanMaterialsLinks", ignore = true)
    @Mapping(target = "lessonPlanTopicLinks", ignore = true)
    LessonPlan toDomainEntity(LessonPlanTable lessonPlanTable);

    @Mapping(target = "lessonPlanMaterialsLinks", ignore = true)
    @Mapping(target = "lessonPlanTopicLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonPlanTable toTableEntity(LessonPlan lessonPlan);
}

