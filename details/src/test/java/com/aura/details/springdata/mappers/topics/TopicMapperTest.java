package com.aura.details.springdata.mappers.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TopicMapper")
class TopicMapperTest {

    private final TopicMapper mapper = TopicMapper.INSTANCE;

    @DisplayName("Should map TopicModel scalar fields into Topic without populating any cross-aggregate collections")
    @Test
    void toDomainEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        TopicModel model = TopicModel.builder()
                .id(id)
                .name("Algebra")
                .tenantId("tenant-1")
                .build();

        Topic topic = mapper.toDomainEntity(model);

        assertThat(topic.getId()).isEqualTo(id.toString());
        assertThat(topic.getName()).isEqualTo("Algebra");
        assertThat(topic.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should map Topic scalar fields back into TopicModel")
    @Test
    void toModelEntity_mapsScalarFieldsOnly() {
        UUID id = UUID.randomUUID();
        Topic topic = Topic.builder()
                .id(id.toString())
                .name("Algebra")
                .tenantId("tenant-1")
                .build();

        TopicModel model = mapper.toModelEntity(topic);

        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Algebra");
        assertThat(model.getTenantId()).isEqualTo("tenant-1");
    }

    @DisplayName("Should preserve fields across a Model->Domain->Model round-trip")
    @Test
    void roundTrip_preservesScalarFields() {
        TopicModel original = TopicModel.builder()
                .id(UUID.randomUUID())
                .name("Algebra")
                .tenantId("tenant-1")
                .build();

        TopicModel roundTripped = mapper.toModelEntity(mapper.toDomainEntity(original));

        assertThat(roundTripped.getId()).isEqualTo(original.getId());
        assertThat(roundTripped.getName()).isEqualTo(original.getName());
        assertThat(roundTripped.getTenantId()).isEqualTo(original.getTenantId());
    }
}
