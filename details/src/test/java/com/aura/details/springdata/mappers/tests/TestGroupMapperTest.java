package com.aura.details.springdata.mappers.tests;

import com.aura.core.entities.tests.TestGroup;
import com.aura.details.springdata.models.tests.TestGroupModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TestGroupMapper")
class TestGroupMapperTest {

    private final TestGroupMapper mapper = TestGroupMapper.INSTANCE;

    @DisplayName("Should map TestGroupModel scalar fields into TestGroup without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        TestGroupModel model = TestGroupModel.builder()
                .id(id)
                .tenantId("tenant-1")
                .build();

        TestGroup testGroup = mapper.toDomainEntity(model);

        assertThat(testGroup.getId()).isEqualTo(id.toString());
        assertThat(testGroup.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map TestGroup scalar fields back into TestGroupModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        TestGroup testGroup = TestGroup.builder()
                .id(id.toString())
                .tenantId("tenant-1")
                .build();

        TestGroupModel model = mapper.toModelEntity(testGroup);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        TestGroupModel original = TestGroupModel.builder()
                .id(UUID.randomUUID())
                .tenantId("tenant-1")
                .build();

        TestGroupModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
