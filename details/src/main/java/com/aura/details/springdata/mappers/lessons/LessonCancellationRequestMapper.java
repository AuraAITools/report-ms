package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonCancellationRequest;
import com.aura.details.springdata.models.lessons.LessonCancellationRequestModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonCancellationRequestMapper {
    LessonCancellationRequestMapper INSTANCE = Mappers.getMapper(LessonCancellationRequestMapper.class);

    @Mapping(source = "lesson.id", target = "lessonId")
    LessonCancellationRequest toDomainEntity(LessonCancellationRequestModel model);

    @Mapping(source = "lessonId", target = "lesson", qualifiedByName = "lessonRef")
    LessonCancellationRequestModel toModelEntity(LessonCancellationRequest request);

    @Named("lessonRef")
    default LessonModel lessonRef(String lessonId) {
        if (lessonId == null) {
            return null;
        }
        return LessonModel.builder().id(UUID.fromString(lessonId)).build();
    }
}
