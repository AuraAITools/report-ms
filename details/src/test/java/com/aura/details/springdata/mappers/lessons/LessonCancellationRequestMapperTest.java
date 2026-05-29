package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonCancellationRequest;
import com.aura.details.springdata.models.lessons.LessonCancellationRequestModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonCancellationRequestMapper")
class LessonCancellationRequestMapperTest {

    private final LessonCancellationRequestMapper mapper = LessonCancellationRequestMapper.INSTANCE;

    @DisplayName("Should map LessonCancellationRequestModel.lesson.id into LessonCancellationRequest.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonCancellationRequestModel model = LessonCancellationRequestModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonCancellationRequestModel.APPROVAL_STATUS.PENDING)
                .build();

        LessonCancellationRequest domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(domain.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(domain.getApprovedBy()).isEqualTo("admin");
        assertThat(domain.getStatus()).isEqualTo(LessonCancellationRequest.APPROVAL_STATUS.PENDING);
    }

    @DisplayName("Should set LessonCancellationRequestModel.lesson to a stub LessonModel populated only with the id from LessonCancellationRequest.lessonId")
    @Test
    void toModelEntity_buildsLessonStubFromLessonId() {
        UUID lessonId = UUID.randomUUID();
        LessonCancellationRequest domain = LessonCancellationRequest.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonId.toString())
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonCancellationRequest.APPROVAL_STATUS.APPROVED)
                .build();

        LessonCancellationRequestModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNotNull();
        assertThat(model.getLesson().getId()).isEqualTo(lessonId);
        assertThat(model.getLesson().getName()).isNull();
        assertThat(model.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(model.getApprovedBy()).isEqualTo("admin");
        assertThat(model.getStatus()).isEqualTo(LessonCancellationRequestModel.APPROVAL_STATUS.APPROVED);
    }

    @DisplayName("Should leave LessonCancellationRequestModel.lesson null when LessonCancellationRequest.lessonId is null")
    @Test
    void toModelEntity_returnsNullLessonWhenLessonIdNull() {
        LessonCancellationRequest domain = LessonCancellationRequest.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(null)
                .build();

        LessonCancellationRequestModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }

    @DisplayName("Should preserve lessonId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonId() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonCancellationRequestModel original = LessonCancellationRequestModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonCancellationRequestModel.APPROVAL_STATUS.PENDING)
                .build();

        LessonCancellationRequestModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLesson().getId()).isEqualTo(lessonId);
        assertThat(roundTripped.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(roundTripped.getApprovedBy()).isEqualTo("admin");
        assertThat(roundTripped.getStatus()).isEqualTo(LessonCancellationRequestModel.APPROVAL_STATUS.PENDING);
    }
}
