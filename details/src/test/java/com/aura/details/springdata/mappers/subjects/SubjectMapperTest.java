package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.details.springdata.models.subjects.SubjectModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SubjectMapper")
class SubjectMapperTest {

    private final SubjectMapper mapper = SubjectMapper.INSTANCE;

    @DisplayName("Should map SubjectModel scalar fields into Subject without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        SubjectModel model = SubjectModel.builder()
                .id(id)
                .name("Mathematics")
                .tenantId("tenant-1")
                .build();

        Subject subject = mapper.toDomainEntity(model);

        assertThat(subject.getId()).isEqualTo(id.toString());
        assertThat(subject.getName()).isEqualTo("Mathematics");
        assertThat(subject.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Subject scalar fields back into SubjectModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Subject subject = Subject.builder()
                .id(id.toString())
                .name("Mathematics")
                .tenantId("tenant-1")
                .build();

        SubjectModel model = mapper.toModelEntity(subject);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Mathematics");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        SubjectModel original = SubjectModel.builder()
                .id(UUID.randomUUID())
                .name("Mathematics")
                .tenantId("tenant-1")
                .build();

        SubjectModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
