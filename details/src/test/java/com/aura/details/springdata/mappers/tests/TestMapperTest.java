package com.aura.details.springdata.mappers.tests;

import com.aura.details.springdata.models.tests.TestGroupModel;
import com.aura.details.springdata.models.tests.TestModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TestMapper")
class TestMapperTest {

    private final TestMapper mapper = TestMapper.INSTANCE;

    @DisplayName("Should map TestModel.testGroup.id into Test.testGroupId without traversing back into TestGroup")
    @Test
    void toDomainEntity_extractsTestGroupIdFromModel() {
        UUID testGroupId = UUID.randomUUID();
        TestGroupModel testGroupModel = TestGroupModel.builder().id(testGroupId).build();
        TestModel model = TestModel.builder()
                .id(UUID.randomUUID())
                .name("Mid-term")
                .testGroup(testGroupModel)
                .build();

        com.aura.core.entities.tests.Test domain = mapper.toDomainEntity(model);

        assertThat(domain.getTestGroupId()).isEqualTo(testGroupId.toString());
        assertThat(domain.getName()).isEqualTo("Mid-term");
    }

    @DisplayName("Should set TestModel.testGroup to a stub TestGroupModel populated only with the id from Test.testGroupId")
    @Test
    void toModelEntity_buildsTestGroupStubFromTestGroupId() {
        UUID testGroupId = UUID.randomUUID();
        com.aura.core.entities.tests.Test domain = com.aura.core.entities.tests.Test.builder()
                .id(UUID.randomUUID().toString())
                .name("Mid-term")
                .testGroupId(testGroupId.toString())
                .build();

        TestModel model = mapper.toModelEntity(domain);

        assertThat(model.getTestGroup()).isNotNull();
        assertThat(model.getTestGroup().getId()).isEqualTo(testGroupId);
        assertThat(model.getName()).isEqualTo("Mid-term");
    }

    @DisplayName("Should leave TestModel.testGroup null when Test.testGroupId is null")
    @Test
    void toModelEntity_returnsNullTestGroupWhenTestGroupIdNull() {
        com.aura.core.entities.tests.Test domain = com.aura.core.entities.tests.Test.builder()
                .id(UUID.randomUUID().toString())
                .testGroupId(null)
                .build();

        TestModel model = mapper.toModelEntity(domain);

        assertThat(model.getTestGroup()).isNull();
    }

    @DisplayName("Should preserve testGroupId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesTestGroupId() {
        UUID testGroupId = UUID.randomUUID();
        TestGroupModel testGroupModel = TestGroupModel.builder().id(testGroupId).build();
        TestModel original = TestModel.builder()
                .id(UUID.randomUUID())
                .name("Mid-term")
                .testGroup(testGroupModel)
                .build();

        TestModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getTestGroup().getId()).isEqualTo(testGroupId);
        assertThat(roundTripped.getName()).isEqualTo("Mid-term");
    }
}
