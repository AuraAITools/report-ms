package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonPlanMapper")
class LessonPlanMapperTest {

    private final LessonPlanMapper mapper = LessonPlanMapper.INSTANCE;

    @DisplayName("Should map LessonPlanModel.lesson.id into LessonPlan.lessonId without traversing back into Lesson")
    @Test
    void toDomainEntity_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonPlanModel model = LessonPlanModel.builder()
                .id(planId)
                .lesson(lessonModel)
                .build();

        LessonPlan domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(domain.getId()).isEqualTo(planId.toString());
    }

    @DisplayName("Should leave LessonPlanModel.lesson null because lesson is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toModelEntity_doesNotBuildLessonStub() {
        UUID lessonId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonPlan domain = LessonPlan.builder()
                .id(planId.toString())
                .lessonId(lessonId.toString())
                .build();

        LessonPlanModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
        assertThat(model.getId()).isEqualTo(planId);
    }

    @DisplayName("Should preserve planId across a Model→Domain→Model round-trip (lesson is not roundtripped through the mapper)")
    @Test
    void roundTrip_preservesPlanId() {
        UUID lessonId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonPlanModel original = LessonPlanModel.builder()
                .id(planId)
                .lesson(lessonModel)
                .build();

        LessonPlanModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLesson()).isNull();
        assertThat(roundTripped.getId()).isEqualTo(planId);
    }
}
