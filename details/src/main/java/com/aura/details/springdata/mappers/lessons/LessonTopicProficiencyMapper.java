package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.details.springdata.models.lessons.LessonTopicProficiencyModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonTopicProficiencyMapper {
    LessonTopicProficiencyMapper INSTANCE = Mappers.getMapper(LessonTopicProficiencyMapper.class);

    @Mapping(source = "topic.id", target = "topicId")
    @Mapping(source = "studentLessonReport.id", target = "studentLessonReportId")
    LessonTopicProficiency toDomainEntity(LessonTopicProficiencyModel model);

    @Mapping(source = "topicId", target = "topic", qualifiedByName = "topicRef")
    @Mapping(source = "studentLessonReportId", target = "studentLessonReport", qualifiedByName = "studentLessonReportRef")
    LessonTopicProficiencyModel toModelEntity(LessonTopicProficiency proficiency);

    @Named("topicRef")
    default TopicModel topicRef(String topicId) {
        if (topicId == null) {
            return null;
        }
        return TopicModel.builder().id(UUID.fromString(topicId)).build();
    }

    @Named("studentLessonReportRef")
    default StudentLessonReportModel studentLessonReportRef(String studentLessonReportId) {
        if (studentLessonReportId == null) {
            return null;
        }
        return StudentLessonReportModel.builder().id(UUID.fromString(studentLessonReportId)).build();
    }
}
