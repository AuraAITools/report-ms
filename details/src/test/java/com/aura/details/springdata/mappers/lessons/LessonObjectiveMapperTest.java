package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonObjectiveMapper")
class LessonObjectiveMapperTest {

    private final LessonObjectiveMapper mapper = LessonObjectiveMapper.INSTANCE;

    @DisplayName("Should map LessonObjectiveModel.lessonPlan.id into LessonObjective.lessonPlanId without traversing back into LessonPlan")
    @Test
    void toDomainEntity_extractsLessonPlanIdFromModel() {
        UUID lessonPlanId = UUID.randomUUID();
        UUID objectiveId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        LessonObjectiveModel model = LessonObjectiveModel.builder()
                .id(objectiveId)
                .lessonPlan(lessonPlanModel)
                .build();

        LessonObjective domain = mapper.toDomainEntity(model);

        assertThat(domain.getLessonPlanId()).isEqualTo(lessonPlanId.toString());
        assertThat(domain.getId()).isEqualTo(objectiveId.toString());
    }

    @DisplayName("Should leave LessonObjectiveModel.lessonPlan null on toModelEntity — attached separately by the repository impl")
    @Test
    void toModelEntity_leavesLessonPlanNull() {
        UUID lessonPlanId = UUID.randomUUID();
        UUID objectiveId = UUID.randomUUID();
        LessonObjective domain = LessonObjective.builder()
                .id(objectiveId.toString())
                .lessonPlanId(lessonPlanId.toString())
                .build();

        LessonObjectiveModel model = mapper.toModelEntity(domain);

        assertThat(model.getLessonPlan()).isNull();
        assertThat(model.getId()).isEqualTo(objectiveId);
    }
}
