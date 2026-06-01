package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonQuizMapper")
class LessonQuizMapperTest {

    private final LessonQuizMapper mapper = LessonQuizMapper.INSTANCE;

    @DisplayName("Should map LessonQuizModel.lessonPlan.id into LessonQuiz.lessonPlanId without traversing back into LessonPlan")
    @Test
    void toDomainEntity_extractsLessonPlanIdFromModel() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        LessonQuizModel model = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(lessonPlanModel)
                .score(85)
                .maxScore(100)
                .build();

        LessonQuiz domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonPlanId()).isEqualTo(lessonPlanId.toString());
        assertThat(domain.getScore()).isEqualTo(85);
        assertThat(domain.getMaxScore()).isEqualTo(100);
    }

    @DisplayName("Should set LessonQuizModel.lessonPlan to a stub LessonPlanModel populated only with the id from LessonQuiz.lessonPlanId")
    @Test
    void toModelEntity_buildsLessonPlanStubFromLessonPlanId() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(lessonPlanId.toString())
                .score(72)
                .maxScore(120)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonPlan()).isNotNull();
        assertThat(model.getLessonPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(model.getLessonPlan().getPlan()).isNull();
        assertThat(model.getScore()).isEqualTo(72);
        assertThat(model.getMaxScore()).isEqualTo(120);
    }

    @DisplayName("Should leave LessonQuizModel.lessonPlan null when LessonQuiz.lessonPlanId is null")
    @Test
    void toModelEntity_returnsNullLessonPlanWhenLessonPlanIdNull() {
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(null)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonPlan()).isNull();
    }

    @DisplayName("Should preserve lessonPlanId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonPlanId() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        LessonQuizModel original = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(lessonPlanModel)
                .score(91)
                .maxScore(100)
                .build();

        LessonQuizModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLessonPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(roundTripped.getScore()).isEqualTo(91);
        assertThat(roundTripped.getMaxScore()).isEqualTo(100);
    }
}
