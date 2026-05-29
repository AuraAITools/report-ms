package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonQuizMapper {
    LessonQuizMapper INSTANCE = Mappers.getMapper(LessonQuizMapper.class);

    @Mapping(source = "lesson.id", target = "lessonId")
    LessonQuiz toDomainEntity(LessonQuizModel model);

    @Mapping(source = "lessonId", target = "lesson", qualifiedByName = "lessonRef")
    LessonQuizModel toModelEntity(LessonQuiz lessonQuiz);

    @Named("lessonRef")
    default LessonModel lessonRef(String lessonId) {
        if (lessonId == null) {
            return null;
        }
        return LessonModel.builder().id(UUID.fromString(lessonId)).build();
    }
}
