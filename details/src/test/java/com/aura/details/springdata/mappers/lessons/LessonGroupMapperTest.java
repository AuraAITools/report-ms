package com.aura.details.springdata.mappers.lessons;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.details.springdata.models.lessons.LessonGroupModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonGroupMapper")
class LessonGroupMapperTest {

    private final LessonGroupMapper mapper = LessonGroupMapper.INSTANCE;

    @DisplayName("Should map lessonGroupModel to LessonGroup domain entity")
    @Test
    void toDomainEntity_mapsAllFieldsIncludingLessonId() {
        UUID lessonId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LessonGroupModel model = LessonGroupModel.builder()
                .id(groupId)
                .lesson(lessonModel)
                .name("My Group")
                .tenantId("tenant-1")
                .build();

        LessonGroup domain = mapper.toDomainEntity(model);

        assertThat(domain.getId()).isEqualTo(groupId.toString());
        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
        assertThat(domain.getName()).isEqualTo("My Group");
        assertThat(domain.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map LessonGroup domain entity to lessonGroupModel ignoring lesson, lessonGroupPlan and registeredStudents")
    @Test
    void toModelEntity_ignoresLessonAndRelationships() {
        UUID groupId = UUID.randomUUID();
        UUID lessonId = UUID.randomUUID();
        LessonGroup domain = LessonGroup.builder()
                .id(groupId.toString())
                .lessonId(lessonId.toString())
                .name("My Group")
                .tenantId("tenant-1")
                .build();

        LessonGroupModel model = mapper.toModelEntity(domain);

        assertThat(model.getId()).isEqualTo(groupId);
        assertThat(model.getName()).isEqualTo("My Group");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
        assertThat(model.getLesson()).isNull();
        assertThat(model.getLessonGroupPlan()).isNull();
        assertThat(model.getRegisteredStudents()).isNullOrEmpty();
    }
}
