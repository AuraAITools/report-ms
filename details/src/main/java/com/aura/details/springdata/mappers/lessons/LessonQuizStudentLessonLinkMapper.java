package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuizStudentLessonLink;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import com.aura.details.springdata.models.lessons.LessonQuizStudentLessonLinkModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonQuizStudentLessonLinkMapper {

    LessonQuizStudentLessonLinkMapper INSTANCE = Mappers.getMapper(LessonQuizStudentLessonLinkMapper.class);

    @Mapping(source = "lessonQuizTable.id", target = "lessonQuizId")
    @Mapping(source = "studentLessonReport.id", target = "studentLessonReportId")
    LessonQuizStudentLessonLink toDomain(LessonQuizStudentLessonLinkModel model);

    @Mapping(source = "lessonQuizId", target = "lessonQuizTable", qualifiedByName = "lessonQuizRef")
    @Mapping(source = "studentLessonReportId", target = "studentLessonReport", qualifiedByName = "studentLessonReportRef")
    LessonQuizStudentLessonLinkModel toTable(LessonQuizStudentLessonLink link);

    @Named("lessonQuizRef")
    default LessonQuizModel lessonQuizRef(String lessonQuizId) {
        if (lessonQuizId == null) {
            return null;
        }
        return LessonQuizModel.builder().id(UUID.fromString(lessonQuizId)).build();
    }

    @Named("studentLessonReportRef")
    default StudentLessonReportModel studentLessonReportRef(String studentLessonReportId) {
        if (studentLessonReportId == null) {
            return null;
        }
        return StudentLessonReportModel.builder().id(UUID.fromString(studentLessonReportId)).build();
    }
}
