package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LevelEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.LevelEducatorLinkModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LevelEducatorLinkMapper")
class LevelEducatorLinkMapperTest {

    private final LevelEducatorLinkMapper mapper = LevelEducatorLinkMapper.INSTANCE;

    @DisplayName("Should map LevelEducatorLinkModel.educator.id into LevelEducatorLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        LevelEducatorLinkModel model = LevelEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        LevelEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should map LevelEducatorLinkModel.level.id into LevelEducatorLink.levelId without traversing back into Level")
    @Test
    void toDomain_extractsLevelIdFromModel() {
        UUID levelId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        LevelEducatorLinkModel model = LevelEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .level(levelModel)
                .build();

        LevelEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getLevelId()).isEqualTo(levelId.toString());
    }

    @DisplayName("Should set LevelEducatorLinkModel.educator to a stub EducatorModel populated only with the id from LevelEducatorLink.educatorId")
    @Test
    void toTable_buildsEducatorStubFromEducatorId() {
        UUID educatorId = UUID.randomUUID();
        LevelEducatorLink domain = LevelEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(educatorId.toString())
                .build();

        LevelEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getEducator()).isNotNull();
        assertThat(model.getEducator().getId()).isEqualTo(educatorId);
        assertThat(model.getEducator().getName()).isNull();
    }

    @DisplayName("Should set LevelEducatorLinkModel.level to a stub LevelModel populated only with the id from LevelEducatorLink.levelId")
    @Test
    void toTable_buildsLevelStubFromLevelId() {
        UUID levelId = UUID.randomUUID();
        LevelEducatorLink domain = LevelEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .levelId(levelId.toString())
                .build();

        LevelEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNotNull();
        assertThat(model.getLevel().getId()).isEqualTo(levelId);
        assertThat(model.getLevel().getName()).isNull();
    }

    @DisplayName("Should leave LevelEducatorLinkModel.educator null when LevelEducatorLink.educatorId is null")
    @Test
    void toTable_returnsNullEducatorWhenEducatorIdNull() {
        LevelEducatorLink domain = LevelEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(null)
                .build();

        LevelEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getEducator()).isNull();
    }

    @DisplayName("Should leave LevelEducatorLinkModel.level null when LevelEducatorLink.levelId is null")
    @Test
    void toTable_returnsNullLevelWhenLevelIdNull() {
        LevelEducatorLink domain = LevelEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .levelId(null)
                .build();

        LevelEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNull();
    }

    @DisplayName("Should preserve educatorId and levelId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesEducatorIdAndLevelId() {
        UUID educatorId = UUID.randomUUID();
        UUID levelId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        LevelEducatorLinkModel original = LevelEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .level(levelModel)
                .build();

        LevelEducatorLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getEducator().getId()).isEqualTo(educatorId);
        assertThat(roundTripped.getLevel().getId()).isEqualTo(levelId);
    }
}
