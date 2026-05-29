package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPostponementRequest;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonPostponementRequestModel;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonPostponementRequestMapper")
class LessonPostponementRequestMapperTest {

    private final LessonPostponementRequestMapper mapper = LessonPostponementRequestMapper.INSTANCE;

    @DisplayName("Should map LessonPostponementRequestModel.lesson.id into LessonPostponementRequest.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-01T09:00:00Z");
        Instant end = Instant.parse("2026-06-01T10:00:00Z");
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonPostponementRequestModel model = LessonPostponementRequestModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonPostponementRequestModel.APPROVAL_STATUS.PENDING)
                .postponedLessonStartTimestamptz(start)
                .postponedLessonEndTimestamptz(end)
                .build();

        LessonPostponementRequest domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(domain.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(domain.getApprovedBy()).isEqualTo("admin");
        assertThat(domain.getStatus()).isEqualTo(LessonPostponementRequest.APPROVAL_STATUS.PENDING);
        assertThat(domain.getPostponedLessonStartTimestamptz()).isEqualTo(start);
        assertThat(domain.getPostponedLessonEndTimestamptz()).isEqualTo(end);
    }

    @DisplayName("Should set LessonPostponementRequestModel.lesson to a stub LessonModel populated only with the id from LessonPostponementRequest.lessonId")
    @Test
    void toModelEntity_buildsLessonStubFromLessonId() {
        UUID lessonId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-01T09:00:00Z");
        Instant end = Instant.parse("2026-06-01T10:00:00Z");
        LessonPostponementRequest domain = LessonPostponementRequest.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonId.toString())
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonPostponementRequest.APPROVAL_STATUS.APPROVED)
                .postponedLessonStartTimestamptz(start)
                .postponedLessonEndTimestamptz(end)
                .build();

        LessonPostponementRequestModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNotNull();
        assertThat(model.getLesson().getId()).isEqualTo(lessonId);
        assertThat(model.getLesson().getName()).isNull();
        assertThat(model.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(model.getApprovedBy()).isEqualTo("admin");
        assertThat(model.getStatus()).isEqualTo(LessonPostponementRequestModel.APPROVAL_STATUS.APPROVED);
        assertThat(model.getPostponedLessonStartTimestamptz()).isEqualTo(start);
        assertThat(model.getPostponedLessonEndTimestamptz()).isEqualTo(end);
    }

    @DisplayName("Should leave LessonPostponementRequestModel.lesson null when LessonPostponementRequest.lessonId is null")
    @Test
    void toModelEntity_returnsNullLessonWhenLessonIdNull() {
        LessonPostponementRequest domain = LessonPostponementRequest.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(null)
                .build();

        LessonPostponementRequestModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }

    @DisplayName("Should preserve lessonId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonId() {
        UUID lessonId = UUID.randomUUID();
        Instant start = Instant.parse("2026-06-01T09:00:00Z");
        Instant end = Instant.parse("2026-06-01T10:00:00Z");
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonPostponementRequestModel original = LessonPostponementRequestModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .reasoning("Educator unavailable")
                .approvedBy("admin")
                .status(LessonPostponementRequestModel.APPROVAL_STATUS.PENDING)
                .postponedLessonStartTimestamptz(start)
                .postponedLessonEndTimestamptz(end)
                .build();

        LessonPostponementRequestModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLesson().getId()).isEqualTo(lessonId);
        assertThat(roundTripped.getReasoning()).isEqualTo("Educator unavailable");
        assertThat(roundTripped.getApprovedBy()).isEqualTo("admin");
        assertThat(roundTripped.getStatus()).isEqualTo(LessonPostponementRequestModel.APPROVAL_STATUS.PENDING);
        assertThat(roundTripped.getPostponedLessonStartTimestamptz()).isEqualTo(start);
        assertThat(roundTripped.getPostponedLessonEndTimestamptz()).isEqualTo(end);
    }
}
