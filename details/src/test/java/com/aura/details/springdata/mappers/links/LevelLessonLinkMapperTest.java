package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LevelLessonLink;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LevelLessonLinkMapper")
class LevelLessonLinkMapperTest {

    private final LevelLessonLinkMapper mapper = LevelLessonLinkMapper.INSTANCE;

    @DisplayName("Should map LevelLessonLinkModel.level.id into LevelLessonLink.levelId without traversing back into Level")
    @Test
    void toDomain_extractsLevelIdFromModel() {
        UUID levelId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        LevelLessonLinkModel model = LevelLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .level(levelModel)
                .build();

        LevelLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLevelId()).isEqualTo(levelId.toString());
    }

    @DisplayName("Should map LevelLessonLinkModel.lesson.id into LevelLessonLink.lessonId without traversing back into Lesson")
    @Test
    void toDomain_extractsLessonIdFromModel() {
        UUID lessonId = UUID.randomUUID();
        LessonModel lessonModel = LessonModel.builder().id(lessonId).build();
        LevelLessonLinkModel model = LevelLessonLinkModel.builder()
                .id(UUID.randomUUID())
                .lesson(lessonModel)
                .build();

        LevelLessonLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonId()).isEqualTo(lessonId.toString());
    }

    @DisplayName("Should leave LevelLessonLinkModel.level null on toModelEntity — LevelLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLevelNullForRepositoryImplToAttach() {
        LevelLessonLink domain = LevelLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .levelId(UUID.randomUUID().toString())
                .build();

        LevelLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNull();
    }

    @DisplayName("Should leave LevelLessonLinkModel.lesson null on toModelEntity — LevelLessonLinkRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesLessonNullForRepositoryImplToAttach() {
        LevelLessonLink domain = LevelLessonLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(UUID.randomUUID().toString())
                .build();

        LevelLessonLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLesson()).isNull();
    }
}
