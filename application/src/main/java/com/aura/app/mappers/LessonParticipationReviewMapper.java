package com.aura.app.mappers;

import com.aura.app.codegen.types.LessonParticipationReviewResponse;
import com.aura.app.codegen.types.ParticipationType;
import com.aura.core.entities.lessons.LessonParticipationReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface LessonParticipationReviewMapper {

    LessonParticipationReviewMapper INSTANCE = Mappers.getMapper(LessonParticipationReviewMapper.class);

    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    LessonParticipationReviewResponse toDto(LessonParticipationReview review);

    default ParticipationType toDtoEnum(LessonParticipationReview.PARTICIPATION_TYPE type) {
        return type == null ? null : ParticipationType.valueOf(type.name());
    }

    default LessonParticipationReview.PARTICIPATION_TYPE toDomainEnum(ParticipationType type) {
        return type == null ? null : LessonParticipationReview.PARTICIPATION_TYPE.valueOf(type.name());
    }
}
