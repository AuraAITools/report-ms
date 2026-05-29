package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPostponementRequest;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonPostponementRequestModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonPostponementRequestMapper {
    LessonPostponementRequestMapper INSTANCE = Mappers.getMapper(LessonPostponementRequestMapper.class);

    @Mapping(source = "lesson.id", target = "lessonId")
    LessonPostponementRequest toDomainEntity(LessonPostponementRequestModel model);

    @Mapping(source = "lessonId", target = "lesson", qualifiedByName = "lessonRef")
    LessonPostponementRequestModel toModelEntity(LessonPostponementRequest request);

    @Named("lessonRef")
    default LessonModel lessonRef(String lessonId) {
        if (lessonId == null) {
            return null;
        }
        return LessonModel.builder().id(UUID.fromString(lessonId)).build();
    }
}
