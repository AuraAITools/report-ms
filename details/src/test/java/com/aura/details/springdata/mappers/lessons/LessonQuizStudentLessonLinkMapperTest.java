package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuizStudentLessonLink;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import com.aura.details.springdata.models.lessons.LessonQuizStudentLessonLinkModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonQuizStudentLessonLinkMapper")
class LessonQuizStudentLessonLinkMapperTest {

    private final LessonQuizStudentLessonLinkMapper mapper = LessonQuizStudentLessonLinkMapper.INSTANCE;

    @DisplayName("Should map LessonQuizStudentLessonLinkModel.lessonQuizTable.id into LessonQuizStudentLessonLink.lessonQuizId without traversing back into LessonQuiz")
    @Test
    void toDomain_extractsLessonQuizIdFromModel() {
        UUID lessonQuizId = UUID.randomUUID();
        LessonQuizModel lessonQuizModel = LessonQuizModel.builder().id(lessonQuizId).build();
        LessonQuizStudentLessonLinkModel model = LessonQuizStudentLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonQuizTable(lessonQuizModel)
                .build();

        LessonQuizStudentLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonQuizId()).isEqualTo(lessonQuizId.toString());
    }

    @DisplayName("Should map LessonQuizStudentLessonLinkModel.studentLessonReport.id into LessonQuizStudentLessonLink.studentLessonReportId without traversing back into StudentLessonReport")
    @Test
    void toDomain_extractsStudentLessonReportIdFromModel() {
        UUID studentLessonReportId = UUID.randomUUID();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder().id(studentLessonReportId).build();
        LessonQuizStudentLessonLinkModel model = LessonQuizStudentLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .studentLessonReport(studentLessonReportModel)
                .build();

        LessonQuizStudentLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getStudentLessonReportId()).isEqualTo(studentLessonReportId.toString());
    }

    @DisplayName("Should set LessonQuizStudentLessonLinkModel.lessonQuizTable to a stub LessonQuizModel populated only with the id from LessonQuizStudentLessonLink.lessonQuizId")
    @Test
    void toTable_buildsLessonQuizStubFromLessonQuizId() {
        UUID lessonQuizId = UUID.randomUUID();
        LessonQuizStudentLessonLink domain = LessonQuizStudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonQuizId(lessonQuizId.toString())
                .build();

        LessonQuizStudentLessonLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonQuizTable()).isNotNull();
        assertThat(model.getLessonQuizTable().getId()).isEqualTo(lessonQuizId);
        assertThat(model.getLessonQuizTable().getLesson()).isNull();
    }

    @DisplayName("Should set LessonQuizStudentLessonLinkModel.studentLessonReport to a stub StudentLessonReportModel populated only with the id from LessonQuizStudentLessonLink.studentLessonReportId")
    @Test
    void toTable_buildsStudentLessonReportStubFromStudentLessonReportId() {
        UUID studentLessonReportId = UUID.randomUUID();
        LessonQuizStudentLessonLink domain = LessonQuizStudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(studentLessonReportId.toString())
                .build();

        LessonQuizStudentLessonLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudentLessonReport()).isNotNull();
        assertThat(model.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
        assertThat(model.getStudentLessonReport().getStudentLessonLink()).isNull();
    }

    @DisplayName("Should leave LessonQuizStudentLessonLinkModel.lessonQuizTable null when LessonQuizStudentLessonLink.lessonQuizId is null")
    @Test
    void toTable_returnsNullLessonQuizWhenLessonQuizIdNull() {
        LessonQuizStudentLessonLink domain = LessonQuizStudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonQuizId(null)
                .build();

        LessonQuizStudentLessonLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonQuizTable()).isNull();
    }

    @DisplayName("Should leave LessonQuizStudentLessonLinkModel.studentLessonReport null when LessonQuizStudentLessonLink.studentLessonReportId is null")
    @Test
    void toTable_returnsNullStudentLessonReportWhenStudentLessonReportIdNull() {
        LessonQuizStudentLessonLink domain = LessonQuizStudentLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(null)
                .build();

        LessonQuizStudentLessonLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudentLessonReport()).isNull();
    }

    @DisplayName("Should preserve lessonQuizId and studentLessonReportId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonQuizIdAndStudentLessonReportId() {
        UUID lessonQuizId = UUID.randomUUID();
        UUID studentLessonReportId = UUID.randomUUID();
        LessonQuizModel lessonQuizModel = LessonQuizModel.builder().id(lessonQuizId).build();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder().id(studentLessonReportId).build();
        LessonQuizStudentLessonLinkModel original = LessonQuizStudentLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonQuizTable(lessonQuizModel)
                .studentLessonReport(studentLessonReportModel)
                .build();

        LessonQuizStudentLessonLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getLessonQuizTable().getId()).isEqualTo(lessonQuizId);
        assertThat(roundTripped.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
    }
}
