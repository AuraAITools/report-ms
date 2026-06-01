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

    @DisplayName("Should map LessonQuizModel.lessonGroupPlan.id into LessonQuiz.lessonGroupPlanId without traversing back into LessonGroupPlan")
    @Test
    void toDomainEntity_extractsLessonPlanIdFromModel() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        LessonQuizModel model = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .score(85)
                .maxScore(100)
                .build();

        LessonQuiz domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonGroupPlanId()).isEqualTo(lessonGroupPlanId.toString());
        assertThat(domain.getScore()).isEqualTo(85);
        assertThat(domain.getMaxScore()).isEqualTo(100);
    }

    @DisplayName("Should set LessonQuizModel.lessonGroupPlan to a stub LessonGroupPlanModel populated only with the id from LessonQuiz.lessonGroupPlanId")
    @Test
    void toModelEntity_buildsLessonGroupPlanStubFromLessonPlanId() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(lessonGroupPlanId.toString())
                .score(72)
                .maxScore(120)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroupPlan()).isNotNull();
        assertThat(model.getLessonGroupPlan().getId()).isEqualTo(lessonGroupPlanId);
        assertThat(model.getLessonGroupPlan().getPlan()).isNull();
        assertThat(model.getScore()).isEqualTo(72);
        assertThat(model.getMaxScore()).isEqualTo(120);
    }

    @DisplayName("Should leave LessonQuizModel.lessonGroupPlan null when LessonQuiz.lessonGroupPlanId is null")
    @Test
    void toModelEntity_returnsNullLessonGroupPlanWhenLessonPlanIdNull() {
        LessonQuiz domain = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(null)
                .build();

        LessonQuizModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroupPlan()).isNull();
    }

    @DisplayName("Should preserve lessonGroupPlanId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonPlanId() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        LessonQuizModel original = LessonQuizModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .score(91)
                .maxScore(100)
                .build();

        LessonQuizModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLessonGroupPlan().getId()).isEqualTo(lessonGroupPlanId);
        assertThat(roundTripped.getScore()).isEqualTo(91);
        assertThat(roundTripped.getMaxScore()).isEqualTo(100);
    }
}
