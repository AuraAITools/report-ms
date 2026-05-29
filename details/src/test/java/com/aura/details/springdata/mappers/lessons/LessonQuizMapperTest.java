package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonQuizMapper")
class LessonQuizMapperTest {

    private final LessonQuizMapper mapper = LessonQuizMapper.INSTANCE;

    @DisplayName("Should map LessonQuizModel.lesson.id into LessonQuiz.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonQuizModel model = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .score(85)
                .maxScore(100)
                .build();

        LessonQuiz domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(domain.getScore()).isEqualTo(85);
        assertThat(domain.getMaxScore()).isEqualTo(100);
    }

    @DisplayName("Should set LessonQuizModel.lesson to a stub LessonModel populated only with the id from LessonQuiz.lessonId")
    @Test
    void toModelEntity_buildsLessonStubFromLessonId() {
        UUID lessonId = UUID.randomUUID();
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonId.toString())
                .score(72)
                .maxScore(120)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNotNull();
        assertThat(model.getLesson().getId()).isEqualTo(lessonId);
        assertThat(model.getLesson().getName()).isNull();
        assertThat(model.getScore()).isEqualTo(72);
        assertThat(model.getMaxScore()).isEqualTo(120);
    }

    @DisplayName("Should leave LessonQuizModel.lesson null when LessonQuiz.lessonId is null")
    @Test
    void toModelEntity_returnsNullLessonWhenLessonIdNull() {
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(null)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }

    @DisplayName("Should preserve lessonId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonId() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonQuizModel original = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .score(91)
                .maxScore(100)
                .build();

        LessonQuizModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLesson().getId()).isEqualTo(lessonId);
        assertThat(roundTripped.getScore()).isEqualTo(91);
        assertThat(roundTripped.getMaxScore()).isEqualTo(100);
    }
}
