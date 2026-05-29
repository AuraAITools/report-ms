package com.aura.details.springdata.mappers.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.models.institutions.InstitutionModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InstitutionMapper")
class InstitutionMapperTest {

    private final InstitutionMapper mapper = InstitutionMapper.INSTANCE;

    @DisplayName("Should map InstitutionModel scalar fields into Institution without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        InstitutionModel model = InstitutionModel.builder()
                .id(id)
                .name("Aura Academy")
                .email("contact@aura.academy")
                .uen("U123")
                .address("1 Aura Lane")
                .contactNumber("91234567")
                .tenantId("tenant-1")
                .build();

        Institution institution = mapper.toDomainEntity(model);

        assertThat(institution.getId()).isEqualTo(id.toString());
        assertThat(institution.getName()).isEqualTo("Aura Academy");
        assertThat(institution.getEmail()).isEqualTo("contact@aura.academy");
        assertThat(institution.getUen()).isEqualTo("U123");
        assertThat(institution.getAddress()).isEqualTo("1 Aura Lane");
        assertThat(institution.getContactNumber()).isEqualTo("91234567");
        assertThat(institution.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Institution scalar fields back into InstitutionModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Institution institution = Institution.builder()
                .id(id.toString())
                .name("Aura Academy")
                .email("contact@aura.academy")
                .uen("U123")
                .address("1 Aura Lane")
                .contactNumber("91234567")
                .tenantId("tenant-1")
                .build();

        InstitutionModel model = mapper.toModelEntity(institution);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Aura Academy");
        assertThat(model.getEmail()).isEqualTo("contact@aura.academy");
        assertThat(model.getUen()).isEqualTo("U123");
        assertThat(model.getAddress()).isEqualTo("1 Aura Lane");
        assertThat(model.getContactNumber()).isEqualTo("91234567");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        InstitutionModel original = InstitutionModel.builder()
                .id(UUID.randomUUID())
                .name("Aura Academy")
                .email("contact@aura.academy")
                .tenantId("tenant-1")
                .build();

        InstitutionModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getEmail()).isEqualTo(original.getEmail());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
