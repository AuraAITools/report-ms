package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonGroupPlanMapper")
class LessonPlanMapperTest {

    private final LessonGroupPlanMapper mapper = LessonGroupPlanMapper.INSTANCE;

    @DisplayName("Should map LessonGroupPlanModel.lessonGroup.id into LessonGroupPlan.lessonGroupId without traversing back into LessonGroup")
    @Test
    void toDomainEntity_extractsLessonGroupIdFromModel() {
        UUID lessonGroupId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonGroupModel lessonGroupModel = LessonGroupModel.builder().id(lessonGroupId).build();
        LessonGroupPlanModel model = LessonGroupPlanModel.builder()
                .id(planId)
                .lessonGroup(lessonGroupModel)
                .build();

        LessonGroupPlan domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonGroupId()).isEqualTo(lessonGroupId.toString());
        assertThat(domain.getId()).isEqualTo(planId.toString());
    }

    @DisplayName("Should leave LessonGroupPlanModel.lessonGroup null because lessonGroup is attached by repository impl via EntityManager.getReference, not by the mapper")
    @Test
    void toModelEntity_doesNotBuildLessonGroupStub() {
        UUID lessonGroupId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonGroupPlan domain = LessonGroupPlan.builder()
                .id(planId.toString())
                .lessonGroupId(lessonGroupId.toString())
                .build();

        LessonGroupPlanModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroup()).isNull();
        assertThat(model.getId()).isEqualTo(planId);
    }

    @DisplayName("Should preserve planId across a Model→Domain→Model round-trip (lessonGroup is not roundtripped through the mapper)")
    @Test
    void roundTrip_preservesPlanId() {
        UUID lessonGroupId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();
        LessonGroupModel lessonGroupModel = LessonGroupModel.builder().id(lessonGroupId).build();
        LessonGroupPlanModel original = LessonGroupPlanModel.builder()
                .id(planId)
                .lessonGroup(lessonGroupModel)
                .build();

        LessonGroupPlanModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getLessonGroup()).isNull();
        assertThat(roundTripped.getId()).isEqualTo(planId);
    }
}
