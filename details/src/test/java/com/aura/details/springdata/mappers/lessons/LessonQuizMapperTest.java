package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonQuizMapper")
class LessonQuizMapperTest {

    private final LessonQuizMapper mapper = LessonQuizMapper.INSTANCE;

    @DisplayName("Should map LessonQuizModel.lessonGroupPlan.id into LessonQuiz.lessonPlanId without traversing back into LessonGroupPlan")
    @Test
    void toDomainEntity_extractsLessonPlanIdFromModel() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonPlanId).build();
        LessonQuizModel model = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .score(85)
                .maxScore(100)
                .build();

        LessonQuiz domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonPlanId()).isEqualTo(lessonPlanId.toString());
        assertThat(domain.getScore()).isEqualTo(85);
        assertThat(domain.getMaxScore()).isEqualTo(100);
    }

    @DisplayName("Should set LessonQuizModel.lessonGroupPlan to a stub LessonGroupPlanModel populated only with the id from LessonQuiz.lessonPlanId")
    @Test
    void toModelEntity_buildsLessonGroupPlanStubFromLessonPlanId() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(lessonPlanId.toString())
                .score(72)
                .maxScore(120)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroupPlan()).isNotNull();
        assertThat(model.getLessonGroupPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(model.getLessonGroupPlan().getPlan()).isNull();
        assertThat(model.getScore()).isEqualTo(72);
        assertThat(model.getMaxScore()).isEqualTo(120);
    }

    @DisplayName("Should leave LessonQuizModel.lessonGroupPlan null when LessonQuiz.lessonPlanId is null")
    @Test
    void toModelEntity_returnsNullLessonGroupPlanWhenLessonPlanIdNull() {
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(null)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroupPlan()).isNull();
    }

    @DisplayName("Should preserve lessonPlanId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonPlanId() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonPlanId).build();
        LessonQuizModel original = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .score(91)
                .maxScore(100)
                .build();

        LessonQuizModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLessonGroupPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(roundTripped.getScore()).isEqualTo(91);
        assertThat(roundTripped.getMaxScore()).isEqualTo(100);
    }
}
