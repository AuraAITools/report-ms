package com.aura.details.springdata.mappers.materials;

import com.aura.core.entities.materials.Material;
import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MaterialMapper")
class MaterialMapperTest {

    private final MaterialMapper mapper = MaterialMapper.INSTANCE;

    @DisplayName("Should map MaterialModel scalar fields into Material without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        MaterialModel model = MaterialModel.builder()
                .id(id)
                .name("Algebra Worksheet")
                .description("Practice problems for chapter 3")
                .fileKey("institutions/tenant-1/materials/abc.pdf")
                .tenantId("tenant-1")
                .build();

        Material material = mapper.toDomainEntity(model);

        assertThat(material.getId()).isEqualTo(id.toString());
        assertThat(material.getName()).isEqualTo("Algebra Worksheet");
        assertThat(material.getDescription()).isEqualTo("Practice problems for chapter 3");
        assertThat(material.getFileKey()).isEqualTo("institutions/tenant-1/materials/abc.pdf");
        assertThat(material.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Material scalar fields back into MaterialModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Material material = Material.builder()
                .id(id.toString())
                .name("Algebra Worksheet")
                .description("Practice problems for chapter 3")
                .fileKey("institutions/tenant-1/materials/abc.pdf")
                .tenantId("tenant-1")
                .build();

        MaterialModel model = mapper.toModelEntity(material);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Algebra Worksheet");
        assertThat(model.getDescription()).isEqualTo("Practice problems for chapter 3");
        assertThat(model.getFileKey()).isEqualTo("institutions/tenant-1/materials/abc.pdf");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        MaterialModel original = MaterialModel.builder()
                .id(UUID.randomUUID())
                .name("Algebra Worksheet")
                .description("Practice problems for chapter 3")
                .fileKey("institutions/tenant-1/materials/abc.pdf")
                .tenantId("tenant-1")
                .build();

        MaterialModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getDescription()).isEqualTo(original.getDescription());
        assertThat(roundTripped.getFileKey()).isEqualTo(original.getFileKey());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
