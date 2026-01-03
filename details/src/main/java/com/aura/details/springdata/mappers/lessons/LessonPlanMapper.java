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
    @Mapping(target = "Id", source = "id")
    LessonPlanTable toTableEntity(LessonPlan lessonPlan);
}

