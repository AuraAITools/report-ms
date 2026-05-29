package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.details.springdata.models.lessons.LessonHomeworkCompletionModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonHomeworkCompletionMapper {
    LessonHomeworkCompletionMapper INSTANCE = Mappers.getMapper(LessonHomeworkCompletionMapper.class);

    @Mapping(source = "studentLessonReport.id", target = "studentLessonReportId")
    LessonHomeworkCompletion toDomainEntity(LessonHomeworkCompletionModel model);

    @Mapping(source = "studentLessonReportId", target = "studentLessonReport", qualifiedByName = "studentLessonReportRef")
    LessonHomeworkCompletionModel toModelEntity(LessonHomeworkCompletion completion);

    @Named("studentLessonReportRef")
    default StudentLessonReportModel studentLessonReportRef(String studentLessonReportId) {
        if (studentLessonReportId == null) {
            return null;
        }
        return StudentLessonReportModel.builder().id(UUID.fromString(studentLessonReportId)).build();
    }
}
