package com.aura.details.springdata.mappers.schools;

import com.aura.core.entities.schools.School;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.details.springdata.models.schools.SchoolModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SchoolMapper")
class SchoolMapperTest {

    private final SchoolMapper mapper = SchoolMapper.INSTANCE;

    @DisplayName("Should map SchoolModel scalar fields into School without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        SchoolModel model = SchoolModel.builder()
                .id(id)
                .name("Greenwood Primary")
                .schoolCategory(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        School school = mapper.toDomainEntity(model);

        assertThat(school.getId()).isEqualTo(id.toString());
        assertThat(school.getName()).isEqualTo("Greenwood Primary");
        assertThat(school.getSchoolCategory()).isEqualTo(SchoolCategory.PRIMARY);
        assertThat(school.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map School scalar fields back into SchoolModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        School school = School.builder()
                .id(id.toString())
                .name("Greenwood Primary")
                .schoolCategory(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        SchoolModel model = mapper.toModelEntity(school);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Greenwood Primary");
        assertThat(model.getSchoolCategory()).isEqualTo(SchoolCategory.PRIMARY);
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        SchoolModel original = SchoolModel.builder()
                .id(UUID.randomUUID())
                .name("Greenwood Primary")
                .schoolCategory(SchoolCategory.PRIMARY)
                .tenantId("tenant-1")
                .build();

        SchoolModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getSchoolCategory()).isEqualTo(original.getSchoolCategory());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
