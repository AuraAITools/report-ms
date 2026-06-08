package com.aura.app.mappers;

import com.aura.app.codegen.types.LessonTopicProficiencyResponse;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonTopicProficiencyMapper {

    LessonTopicProficiencyMapper INSTANCE = Mappers.getMapper(LessonTopicProficiencyMapper.class);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonTopicProficiencyResponse toDto(LessonTopicProficiency proficiency);
}
