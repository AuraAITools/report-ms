package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.details.springdata.models.lessons.LessonHomeworkCompletionModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonHomeworkCompletionMapper")
class LessonHomeworkCompletionMapperTest {

    private final LessonHomeworkCompletionMapper mapper = LessonHomeworkCompletionMapper.INSTANCE;

    @DisplayName("Should map LessonHomeworkCompletionModel.studentLessonReport.id into LessonHomeworkCompletion.studentLessonReportId without traversing back into StudentLessonReport")
    @Test
    void toDomainEntity_extractsStudentLessonReportIdFromModel() {
        UUID studentLessonReportId = UUID.randomUUID();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder()
                .id(studentLessonReportId)
                .build();
        LessonHomeworkCompletionModel model = LessonHomeworkCompletionModel.builder()
                .id(UUID.randomUUID())
                .studentLessonReport(studentLessonReportModel)
                .completion(85)
                .build();

        LessonHomeworkCompletion domain = mapper.toDomainEntity(model);

        assertThat(domain.getStudentLessonReportId()).isEqualTo(studentLessonReportId.toString());
        assertThat(domain.getCompletion()).isEqualTo(85);
    }

    @DisplayName("Should set LessonHomeworkCompletionModel.studentLessonReport to a stub StudentLessonReportModel populated only with the id from LessonHomeworkCompletion.studentLessonReportId")
    @Test
    void toModelEntity_buildsStudentLessonReportStubFromStudentLessonReportId() {
        UUID studentLessonReportId = UUID.randomUUID();
        LessonHomeworkCompletion domain = LessonHomeworkCompletion.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(studentLessonReportId.toString())
                .completion(75)
                .build();

        LessonHomeworkCompletionModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudentLessonReport()).isNotNull();
        assertThat(model.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
        assertThat(model.getStudentLessonReport().getStudentLessonLink()).isNull();
        assertThat(model.getCompletion()).isEqualTo(75);
    }

    @DisplayName("Should leave LessonHomeworkCompletionModel.studentLessonReport null when LessonHomeworkCompletion.studentLessonReportId is null")
    @Test
    void toModelEntity_returnsNullStudentLessonReportWhenStudentLessonReportIdNull() {
        LessonHomeworkCompletion domain = LessonHomeworkCompletion.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(null)
                .build();

        LessonHomeworkCompletionModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudentLessonReport()).isNull();
    }

    @DisplayName("Should preserve studentLessonReportId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesStudentLessonReportId() {
        UUID studentLessonReportId = UUID.randomUUID();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder()
                .id(studentLessonReportId)
                .build();
        LessonHomeworkCompletionModel original = LessonHomeworkCompletionModel.builder()
                .id(UUID.randomUUID())
                .studentLessonReport(studentLessonReportModel)
                .completion(90)
                .build();

        LessonHomeworkCompletionModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
        assertThat(roundTripped.getCompletion()).isEqualTo(90);
    }
}
