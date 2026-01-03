package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.details.springdata.mappers.links.StudentLessonLinkMapper;
import com.aura.details.springdata.tables.lessons.LessonParticipationReviewTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentLessonLinkMapper.class})
public interface LessonParticipationReviewMapper {
    LessonParticipationReviewMapper INSTANCE = Mappers.getMapper(LessonParticipationReviewMapper.class);

    LessonParticipationReview toDomainEntity(LessonParticipationReviewTable lessonParticipationReviewTable);

    @Mapping(target = "Id", source = "id")
    LessonParticipationReviewTable toTableEntity(LessonParticipationReview lessonParticipationReview);
}

