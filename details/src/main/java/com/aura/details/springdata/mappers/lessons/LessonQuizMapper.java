package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonQuizMapper {
    LessonQuizMapper INSTANCE = Mappers.getMapper(LessonQuizMapper.class);

    @Mapping(source = "lessonGroupPlan.id", target = "lessonPlanId")
    LessonQuiz toDomainEntity(LessonQuizModel model);

    @Mapping(source = "lessonPlanId", target = "lessonGroupPlan", qualifiedByName = "lessonGroupPlanRef")
    LessonQuizModel toModelEntity(LessonQuiz lessonQuiz);

    @Named("lessonGroupPlanRef")
    default LessonGroupPlanModel lessonGroupPlanRef(String lessonGroupPlanId) {
        if (lessonGroupPlanId == null) {
            return null;
        }
        return LessonGroupPlanModel.builder().id(UUID.fromString(lessonGroupPlanId)).build();
    }
}
