package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.details.springdata.models.lessons.LessonParticipationReviewModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonParticipationReviewMapper {
    LessonParticipationReviewMapper INSTANCE = Mappers.getMapper(LessonParticipationReviewMapper.class);

    @Mapping(source = "studentLessonReport.id", target = "studentLessonReportId")
    LessonParticipationReview toDomainEntity(LessonParticipationReviewModel model);

    @Mapping(source = "studentLessonReportId", target = "studentLessonReport", qualifiedByName = "studentLessonReportRef")
    LessonParticipationReviewModel toModelEntity(LessonParticipationReview review);

    @Named("studentLessonReportRef")
    default StudentLessonReportModel studentLessonReportRef(String studentLessonReportId) {
        if (studentLessonReportId == null) {
            return null;
        }
        return StudentLessonReportModel.builder().id(UUID.fromString(studentLessonReportId)).build();
    }
}
