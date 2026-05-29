package com.aura.details.springdata.mappers.outlets;

import com.aura.core.entities.outlets.Outlet;
import com.aura.details.springdata.models.outlets.OutletModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OutletMapper")
class OutletMapperTest {

    private final OutletMapper mapper = OutletMapper.INSTANCE;

    @DisplayName("Should map OutletModel scalar fields into Outlet without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        OutletModel model = OutletModel.builder()
                .id(id)
                .name("Sunset Branch")
                .address("1 Sunset Rd")
                .postalCode("123456")
                .contactNumber("91234567")
                .description("Main branch")
                .email("sunset@aura.academy")
                .tenantId("tenant-1")
                .build();

        Outlet outlet = mapper.toDomainEntity(model);

        assertThat(outlet.getId()).isEqualTo(id.toString());
        assertThat(outlet.getName()).isEqualTo("Sunset Branch");
        assertThat(outlet.getAddress()).isEqualTo("1 Sunset Rd");
        assertThat(outlet.getPostalCode()).isEqualTo("123456");
        assertThat(outlet.getContactNumber()).isEqualTo("91234567");
        assertThat(outlet.getDescription()).isEqualTo("Main branch");
        assertThat(outlet.getEmail()).isEqualTo("sunset@aura.academy");
        assertThat(outlet.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Outlet scalar fields back into OutletModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Outlet outlet = Outlet.builder()
                .id(id.toString())
                .name("Sunset Branch")
                .address("1 Sunset Rd")
                .postalCode("123456")
                .contactNumber("91234567")
                .description("Main branch")
                .email("sunset@aura.academy")
                .tenantId("tenant-1")
                .build();

        OutletModel model = mapper.toModelEntity(outlet);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Sunset Branch");
        assertThat(model.getAddress()).isEqualTo("1 Sunset Rd");
        assertThat(model.getPostalCode()).isEqualTo("123456");
        assertThat(model.getContactNumber()).isEqualTo("91234567");
        assertThat(model.getDescription()).isEqualTo("Main branch");
        assertThat(model.getEmail()).isEqualTo("sunset@aura.academy");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        OutletModel original = OutletModel.builder()
                .id(UUID.randomUUID())
                .name("Sunset Branch")
                .address("1 Sunset Rd")
                .email("sunset@aura.academy")
                .tenantId("tenant-1")
                .build();

        OutletModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getAddress()).isEqualTo(original.getAddress());
        assertThat(roundTripped.getEmail()).isEqualTo(original.getEmail());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
