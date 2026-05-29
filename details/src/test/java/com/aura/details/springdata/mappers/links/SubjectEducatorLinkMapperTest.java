package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectEducatorLink;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectEducatorLinkMapper")
class SubjectEducatorLinkMapperTest {

    private final SubjectEducatorLinkMapper mapper = SubjectEducatorLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectEducatorLinkModel.educator.id into SubjectEducatorLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        SubjectEducatorLinkModel model = SubjectEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        SubjectEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should map SubjectEducatorLinkModel.subject.id into SubjectEducatorLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectEducatorLinkModel model = SubjectEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should set SubjectEducatorLinkModel.educator to a stub EducatorModel populated only with the id from SubjectEducatorLink.educatorId")
    @Test
    void toTable_buildsEducatorStubFromEducatorId() {
        UUID educatorId = UUID.randomUUID();
        SubjectEducatorLink domain = SubjectEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(educatorId.toString())
                .build();

        SubjectEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getEducator()).isNotNull();
        assertThat(model.getEducator().getId()).isEqualTo(educatorId);
        assertThat(model.getEducator().getName()).isNull();
    }

    @DisplayName("Should set SubjectEducatorLinkModel.subject to a stub SubjectModel populated only with the id from SubjectEducatorLink.subjectId")
    @Test
    void toTable_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        SubjectEducatorLink domain = SubjectEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        SubjectEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should leave SubjectEducatorLinkModel.educator null when SubjectEducatorLink.educatorId is null")
    @Test
    void toTable_returnsNullEducatorWhenEducatorIdNull() {
        SubjectEducatorLink domain = SubjectEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(null)
                .build();

        SubjectEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getEducator()).isNull();
    }

    @DisplayName("Should leave SubjectEducatorLinkModel.subject null when SubjectEducatorLink.subjectId is null")
    @Test
    void toTable_returnsNullSubjectWhenSubjectIdNull() {
        SubjectEducatorLink domain = SubjectEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        SubjectEducatorLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should preserve educatorId and subjectId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesEducatorIdAndSubjectId() {
        UUID educatorId = UUID.randomUUID();
        UUID subjectId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectEducatorLinkModel original = SubjectEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .subject(subjectModel)
                .build();

        SubjectEducatorLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getEducator().getId()).isEqualTo(educatorId);
        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
    }
}
