package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLessonGroupForLessonInput;
import com.aura.app.codegen.types.CreateLessonGroupInLessonInput;
import com.aura.app.codegen.types.LessonGroupResponse;
import com.aura.core.entities.lessons.LessonGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonGroupMapper {

    LessonGroupMapper INSTANCE = Mappers.getMapper(LessonGroupMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonGroup toDomainEntity(CreateLessonGroupInLessonInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lessonId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    LessonGroup toDomainEntity(CreateLessonGroupForLessonInput input);

    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "lessonGroupPlan", ignore = true)
    @Mapping(target = "lessonStudents", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonGroupResponse toDto(LessonGroup group);
}
