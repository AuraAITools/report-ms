package com.aura.details.mappers.lessons;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.details.mappers.links.StudentLessonLinkMapper;
import com.aura.details.tables.lessons.LessonParticipationReviewTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentLessonLinkMapper.class})
public interface LessonParticipationReviewMapper {
    LessonParticipationReviewMapper INSTANCE = Mappers.getMapper(LessonParticipationReviewMapper.class);

    LessonParticipationReview toDomainEntity(LessonParticipationReviewTable lessonParticipationReviewTable);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LessonParticipationReviewTable toTableEntity(LessonParticipationReview lessonParticipationReview);
}

