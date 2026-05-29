package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.details.springdata.models.lessons.LessonParticipationReviewModel;
import com.aura.details.springdata.models.links.StudentLessonReportModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonParticipationReviewMapper")
class LessonParticipationReviewMapperTest {

    private final LessonParticipationReviewMapper mapper = LessonParticipationReviewMapper.INSTANCE;

    @DisplayName("Should map LessonParticipationReviewModel.studentLessonReport.id into LessonParticipationReview.studentLessonReportId without traversing back into StudentLessonReport")
    @Test
    void toDomainEntity_extractsStudentLessonReportIdFromModel() {
        UUID studentLessonReportId = UUID.randomUUID();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder()
                .id(studentLessonReportId)
                .build();
        LessonParticipationReviewModel model = LessonParticipationReviewModel.builder()
                .id(UUID.randomUUID())
                .studentLessonReport(studentLessonReportModel)
                .participationType(LessonParticipationReviewModel.PARTICIPATION_TYPE.ATTENTIVE)
                .build();

        LessonParticipationReview domain = mapper.toDomainEntity(model);

        assertThat(domain.getStudentLessonReportId()).isEqualTo(studentLessonReportId.toString());
        assertThat(domain.getParticipationType()).isEqualTo(LessonParticipationReview.PARTICIPATION_TYPE.ATTENTIVE);
    }

    @DisplayName("Should set LessonParticipationReviewModel.studentLessonReport to a stub StudentLessonReportModel populated only with the id from LessonParticipationReview.studentLessonReportId")
    @Test
    void toModelEntity_buildsStudentLessonReportStubFromStudentLessonReportId() {
        UUID studentLessonReportId = UUID.randomUUID();
        LessonParticipationReview domain = LessonParticipationReview.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(studentLessonReportId.toString())
                .participationType(LessonParticipationReview.PARTICIPATION_TYPE.CURIOUS)
                .build();

        LessonParticipationReviewModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudentLessonReport()).isNotNull();
        assertThat(model.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
        assertThat(model.getStudentLessonReport().getStudentLessonLink()).isNull();
        assertThat(model.getParticipationType()).isEqualTo(LessonParticipationReviewModel.PARTICIPATION_TYPE.CURIOUS);
    }

    @DisplayName("Should leave LessonParticipationReviewModel.studentLessonReport null when LessonParticipationReview.studentLessonReportId is null")
    @Test
    void toModelEntity_returnsNullStudentLessonReportWhenStudentLessonReportIdNull() {
        LessonParticipationReview domain = LessonParticipationReview.builder()
                .id(UUID.randomUUID().toString())
                .studentLessonReportId(null)
                .build();

        LessonParticipationReviewModel model = mapper.toModelEntity(domain);

        assertThat(model.getStudentLessonReport()).isNull();
    }

    @DisplayName("Should preserve studentLessonReportId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesStudentLessonReportId() {
        UUID studentLessonReportId = UUID.randomUUID();
        StudentLessonReportModel studentLessonReportModel = StudentLessonReportModel.builder()
                .id(studentLessonReportId)
                .build();
        LessonParticipationReviewModel original = LessonParticipationReviewModel.builder()
                .id(UUID.randomUUID())
                .studentLessonReport(studentLessonReportModel)
                .participationType(LessonParticipationReviewModel.PARTICIPATION_TYPE.QUIET)
                .build();

        LessonParticipationReviewModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getStudentLessonReport().getId()).isEqualTo(studentLessonReportId);
        assertThat(roundTripped.getParticipationType()).isEqualTo(LessonParticipationReviewModel.PARTICIPATION_TYPE.QUIET);
    }
}
