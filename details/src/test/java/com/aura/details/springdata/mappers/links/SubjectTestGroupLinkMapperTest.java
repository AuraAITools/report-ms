package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.SubjectTestGroupLink;
import com.aura.details.springdata.models.links.SubjectTestGroupLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.tests.TestGroupModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectTestGroupLinkMapper")
class SubjectTestGroupLinkMapperTest {

    private final SubjectTestGroupLinkMapper mapper = SubjectTestGroupLinkMapper.INSTANCE;

    @DisplayName("Should map SubjectTestGroupLinkModel.subject.id into SubjectTestGroupLink.subjectId without traversing back into Subject")
    @Test
    void toDomain_extractsSubjectIdFromModel() {
        UUID subjectId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        SubjectTestGroupLinkModel model = SubjectTestGroupLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .build();

        SubjectTestGroupLink domain = mapper.toDomain(model);

        assertThat(domain.getSubjectId()).isEqualTo(subjectId.toString());
    }

    @DisplayName("Should map SubjectTestGroupLinkModel.testGroup.id into SubjectTestGroupLink.testGroupId without traversing back into TestGroup")
    @Test
    void toDomain_extractsTestGroupIdFromModel() {
        UUID testGroupId = UUID.randomUUID();
        TestGroupModel testGroupModel = TestGroupModel.builder().id(testGroupId).build();
        SubjectTestGroupLinkModel model = SubjectTestGroupLinkModel.builder()
                .id(UUID.randomUUID())
                .testGroup(testGroupModel)
                .build();

        SubjectTestGroupLink domain = mapper.toDomain(model);

        assertThat(domain.getTestGroupId()).isEqualTo(testGroupId.toString());
    }

    @DisplayName("Should set SubjectTestGroupLinkModel.subject to a stub SubjectModel populated only with the id from SubjectTestGroupLink.subjectId")
    @Test
    void toTable_buildsSubjectStubFromSubjectId() {
        UUID subjectId = UUID.randomUUID();
        SubjectTestGroupLink domain = SubjectTestGroupLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(subjectId.toString())
                .build();

        SubjectTestGroupLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNotNull();
        assertThat(model.getSubject().getId()).isEqualTo(subjectId);
        assertThat(model.getSubject().getName()).isNull();
    }

    @DisplayName("Should set SubjectTestGroupLinkModel.testGroup to a stub TestGroupModel populated only with the id from SubjectTestGroupLink.testGroupId")
    @Test
    void toTable_buildsTestGroupStubFromTestGroupId() {
        UUID testGroupId = UUID.randomUUID();
        SubjectTestGroupLink domain = SubjectTestGroupLink.builder()
                .id(UUID.randomUUID().toString())
                .testGroupId(testGroupId.toString())
                .build();

        SubjectTestGroupLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTestGroup()).isNotNull();
        assertThat(model.getTestGroup().getId()).isEqualTo(testGroupId);
        assertThat(model.getTestGroup().getTenantId()).isNull();
    }

    @DisplayName("Should leave SubjectTestGroupLinkModel.subject null when SubjectTestGroupLink.subjectId is null")
    @Test
    void toTable_returnsNullSubjectWhenSubjectIdNull() {
        SubjectTestGroupLink domain = SubjectTestGroupLink.builder()
                .id(UUID.randomUUID().toString())
                .subjectId(null)
                .build();

        SubjectTestGroupLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getSubject()).isNull();
    }

    @DisplayName("Should leave SubjectTestGroupLinkModel.testGroup null when SubjectTestGroupLink.testGroupId is null")
    @Test
    void toTable_returnsNullTestGroupWhenTestGroupIdNull() {
        SubjectTestGroupLink domain = SubjectTestGroupLink.builder()
                .id(UUID.randomUUID().toString())
                .testGroupId(null)
                .build();

        SubjectTestGroupLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTestGroup()).isNull();
    }

    @DisplayName("Should preserve subjectId and testGroupId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesSubjectIdAndTestGroupId() {
        UUID subjectId = UUID.randomUUID();
        UUID testGroupId = UUID.randomUUID();
        SubjectModel subjectModel = SubjectModel.builder().id(subjectId).build();
        TestGroupModel testGroupModel = TestGroupModel.builder().id(testGroupId).build();
        SubjectTestGroupLinkModel original = SubjectTestGroupLinkModel.builder()
                .id(UUID.randomUUID())
                .subject(subjectModel)
                .testGroup(testGroupModel)
                .build();

        SubjectTestGroupLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getSubject().getId()).isEqualTo(subjectId);
        assertThat(roundTripped.getTestGroup().getId()).isEqualTo(testGroupId);
    }
}
