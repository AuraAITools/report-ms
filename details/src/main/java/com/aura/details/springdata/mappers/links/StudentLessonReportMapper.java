package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.StudentLessonReport;
import com.aura.details.springdata.mappers.lessons.LessonHomeworkCompletionMapper;
import com.aura.details.springdata.mappers.lessons.LessonParticipationReviewMapper;
import com.aura.details.springdata.mappers.lessons.LessonTopicProficiencyMapper;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        LessonHomeworkCompletionMapper.class,
        LessonParticipationReviewMapper.class,
        LessonTopicProficiencyMapper.class
})
public interface StudentLessonReportMapper {
    StudentLessonReportMapper INSTANCE = Mappers.getMapper(StudentLessonReportMapper.class);

    @Mapping(source = "studentLessonLink.id", target = "studentLessonLinkId")
    @Mapping(source = "lessonTopicProficiencies", target = "lessonTopicProficiencies")
    StudentLessonReport toDomainEntity(StudentLessonReportModel model);

    @Mapping(source = "studentLessonLinkId", target = "studentLessonLink", qualifiedByName = "studentLessonLinkRef")
    @Mapping(source = "lessonTopicProficiencies", target = "lessonTopicProficiencies")
    StudentLessonReportModel toModelEntity(StudentLessonReport studentLessonReport);

    @Named("studentLessonLinkRef")
    default StudentLessonLinkModel studentLessonLinkRef(String studentLessonLinkId) {
        if (studentLessonLinkId == null) {
            return null;
        }
        return StudentLessonLinkModel.builder().id(UUID.fromString(studentLessonLinkId)).build();
    }
}
