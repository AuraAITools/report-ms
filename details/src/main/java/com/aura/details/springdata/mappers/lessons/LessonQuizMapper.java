package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonQuizMapper {
    LessonQuizMapper INSTANCE = Mappers.getMapper(LessonQuizMapper.class);

    @Mapping(source = "lessonPlan.id", target = "lessonPlanId")
    LessonQuiz toDomainEntity(LessonQuizModel model);

    @Mapping(source = "lessonPlanId", target = "lessonPlan", qualifiedByName = "lessonPlanRef")
    LessonQuizModel toModelEntity(LessonQuiz lessonQuiz);

    @Named("lessonPlanRef")
    default LessonPlanModel lessonPlanRef(String lessonPlanId) {
        if (lessonPlanId == null) {
            return null;
        }
        return LessonPlanModel.builder().id(UUID.fromString(lessonPlanId)).build();
    }
}
