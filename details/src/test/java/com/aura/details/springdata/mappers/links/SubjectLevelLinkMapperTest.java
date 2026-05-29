package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectLevelLink;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectLevelLinkMapper")
class SubjectLevelLinkMapperTest {

    private final SubjectLevelLinkMapper mapper = SubjectLevelLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectLevelLinkModel.level.id into SubjectLevelLink.levelId without traversing back into Level")
    @Test
    void toDomain_extractsLevelIdFromModel() {
        UUID levelId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        SubjectLevelLinkModel model = SubjectLevelLinkModel.builder()
                .id(UUID.randomUUID())
                .level(levelModel)
                .build();

        SubjectLevelLink domain = mapper.toDomain(model);

        assertThat(domain.getLevelId()).isEqualTo(levelId.toString());
    }

    @DisplayName("Should map SubjectLevelLinkModel.subject.id into SubjectLevelLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectLevelLinkModel model = SubjectLevelLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectLevelLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should set SubjectLevelLinkModel.level to a stub LevelModel populated only with the id from SubjectLevelLink.levelId")
    @Test
    void toTable_buildsLevelStubFromLevelId() {
        UUID levelId = UUID.randomUUID();
        SubjectLevelLink domain = SubjectLevelLink.builder()
                .id(UUID.randomUUID().toString())
                .levelId(levelId.toString())
                .build();

        SubjectLevelLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNotNull();
        assertThat(model.getLevel().getId()).isEqualTo(levelId);
        assertThat(model.getLevel().getName()).isNull();
    }

    @DisplayName("Should set SubjectLevelLinkModel.subject to a stub SubjectModel populated only with the id from SubjectLevelLink.subjectId")
    @Test
    void toTable_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        SubjectLevelLink domain = SubjectLevelLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        SubjectLevelLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should leave SubjectLevelLinkModel.level null when SubjectLevelLink.levelId is null")
    @Test
    void toTable_returnsNullLevelWhenLevelIdNull() {
        SubjectLevelLink domain = SubjectLevelLink.builder()
                .id(UUID.randomUUID().toString())
                .levelId(null)
                .build();

        SubjectLevelLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getLevel()).isNull();
    }

    @DisplayName("Should leave SubjectLevelLinkModel.subject null when SubjectLevelLink.subjectId is null")
    @Test
    void toTable_returnsNullSubjectWhenSubjectIdNull() {
        SubjectLevelLink domain = SubjectLevelLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        SubjectLevelLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should preserve levelId and subjectId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLevelIdAndSubjectId() {
        UUID levelId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        LevelModel levelModel = LevelModel.builder().id(levelId).build();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectLevelLinkModel original = SubjectLevelLinkModel.builder()
                .id(UUID.randomUUID())
                .level(levelModel)
                .subject(subjectModel)
                .build();

        SubjectLevelLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getLevel().getId()).isEqualTo(levelId);
        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
    }
}
