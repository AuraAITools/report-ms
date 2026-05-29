package com.aura.details.springdata.mappers.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.details.springdata.models.educators.EducatorModel;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EducatorMapper")
class EducatorMapperTest {

    private final EducatorMapper mapper = EducatorMapper.INSTANCE;

    @DisplayName("Should map EducatorModel scalar fields into Educator without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        EducatorModel model = EducatorModel.builder()
                .id(id)
                .name("Jane Doe")
                .startDate(LocalDate.of(2024, 1, 15))
                .dateOfBirth(LocalDate.of(1990, 5, 20))
                .email("jane@aura.academy")
                .employmentType(EducatorModel.EMPLOYMENT_TYPE.FULL_TIME)
                .tenantId("tenant-1")
                .build();

        Educator educator = mapper.toDomainEntity(model);

        assertThat(educator.getId()).isEqualTo(id.toString());
        assertThat(educator.getName()).isEqualTo("Jane Doe");
        assertThat(educator.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(educator.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 5, 20));
        assertThat(educator.getEmail()).isEqualTo("jane@aura.academy");
        assertThat(educator.getEmploymentType()).isEqualTo(Educator.EMPLOYMENT_TYPE.FULL_TIME);
        assertThat(educator.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Educator scalar fields back into EducatorModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Educator educator = Educator.builder()
                .id(id.toString())
                .name("Jane Doe")
                .startDate(LocalDate.of(2024, 1, 15))
                .dateOfBirth(LocalDate.of(1990, 5, 20))
                .email("jane@aura.academy")
                .employmentType(Educator.EMPLOYMENT_TYPE.FULL_TIME)
                .tenantId("tenant-1")
                .build();

        EducatorModel model = mapper.toModelEntity(educator);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Jane Doe");
        assertThat(model.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(model.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 5, 20));
        assertThat(model.getEmail()).isEqualTo("jane@aura.academy");
        assertThat(model.getEmploymentType()).isEqualTo(EducatorModel.EMPLOYMENT_TYPE.FULL_TIME);
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        EducatorModel original = EducatorModel.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .startDate(LocalDate.of(2024, 1, 15))
                .dateOfBirth(LocalDate.of(1990, 5, 20))
                .email("jane@aura.academy")
                .employmentType(EducatorModel.EMPLOYMENT_TYPE.PART_TIME)
                .tenantId("tenant-1")
                .build();

        EducatorModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getStartDate()).isEqualTo(original.getStartDate());
        assertThat(roundTripped.getDateOfBirth()).isEqualTo(original.getDateOfBirth());
        assertThat(roundTripped.getEmail()).isEqualTo(original.getEmail());
        assertThat(roundTripped.getEmploymentType()).isEqualTo(original.getEmploymentType());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
