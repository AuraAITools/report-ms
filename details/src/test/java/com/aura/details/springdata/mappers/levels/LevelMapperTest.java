package com.aura.details.springdata.mappers.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.levels.LevelModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LevelMapper")
class LevelMapperTest {

    private final LevelMapper mapper = LevelMapper.INSTANCE;

    @DisplayName("Should map LevelModel scalar fields into Level without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        LevelModel model = LevelModel.builder()
                .id(id)
                .name("Primary 1")
                .category(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        Level level = mapper.toDomainEntity(model);

        assertThat(level.getId()).isEqualTo(id.toString());
        assertThat(level.getName()).isEqualTo("Primary 1");
        assertThat(level.getCategory()).isEqualTo(SchoolCategory.PRIMARY);
        assertThat(level.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Level scalar fields back into LevelModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Level level = Level.builder()
                .id(id.toString())
                .name("Primary 1")
                .category(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        LevelModel model = mapper.toModelEntity(level);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Primary 1");
        assertThat(model.getCategory()).isEqualTo(SchoolCategory.PRIMARY);
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        LevelModel original = LevelModel.builder()
                .id(UUID.randomUUID())
                .name("Primary 1")
                .category(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        LevelModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getCategory()).isEqualTo(original.getCategory());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
