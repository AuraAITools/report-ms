package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonObjectiveMapper")
class LessonObjectiveMapperTest {

    private final LessonObjectiveMapper mapper = LessonObjectiveMapper.INSTANCE;

    @DisplayName("Should map LessonObjectiveModel.lessonGroupPlan.id into LessonObjective.lessonGroupPlanId without traversing back into LessonGroupPlan")
    @Test
    void toDomainEntity_extractsLessonGroupPlanIdFromModel() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        UUID objectiveId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        LessonObjectiveModel model = LessonObjectiveModel.builder()
                .id(objectiveId)
                .lessonGroupPlan(lessonGroupPlanModel)
                .build();

        LessonObjective domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonGroupPlanId()).isEqualTo(lessonGroupPlanId.toString());
        assertThat(domain.getId()).isEqualTo(objectiveId.toString());
    }

    @DisplayName("Should leave LessonObjectiveModel.lessonGroupPlan null on toModelEntity — attached separately by the repository impl")
    @Test
    void toModelEntity_leavesLessonGroupPlanNull() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        UUID objectiveId = UUID.randomUUID();
        LessonObjective domain = LessonObjective.builder()
                .id(objectiveId.toString())
                .lessonGroupPlanId(lessonGroupPlanId.toString())
                .build();

        LessonObjectiveModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonGroupPlan()).isNull();
        assertThat(model.getId()).isEqualTo(objectiveId);
    }
}
