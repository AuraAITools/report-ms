package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.models.topics.TopicModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MaterialTopicLinkMapper")
class MaterialTopicLinkMapperTest {

    private final MaterialTopicLinkMapper mapper = MaterialTopicLinkMapper.INSTANCE;

    @DisplayName("Should map MaterialTopicLinkModel.topic.id into MaterialTopicLink.topicId without traversing back into Topic")
    @Test
    void toDomain_extractsTopicIdFromModel() {
        UUID topicId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        MaterialTopicLinkModel model = MaterialTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .build();

        MaterialTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getTopicId()).isEqualTo(topicId.toString());
    }

    @DisplayName("Should map MaterialTopicLinkModel.material.id into MaterialTopicLink.materialId without traversing back into Material")
    @Test
    void toDomain_extractsMaterialIdFromModel() {
        UUID materialId = UUID.randomUUID();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        MaterialTopicLinkModel model = MaterialTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .material(materialModel)
                .build();

        MaterialTopicLink domain = mapper.toDomain(model);

        assertThat(domain.getMaterialId()).isEqualTo(materialId.toString());
    }

    @DisplayName("Should set MaterialTopicLinkModel.topic to a stub TopicModel populated only with the id from MaterialTopicLink.topicId")
    @Test
    void toTable_buildsTopicStubFromTopicId() {
        UUID topicId = UUID.randomUUID();
        MaterialTopicLink domain = MaterialTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(topicId.toString())
                .build();

        MaterialTopicLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNotNull();
        assertThat(model.getTopic().getId()).isEqualTo(topicId);
        assertThat(model.getTopic().getName()).isNull();
    }

    @DisplayName("Should set MaterialTopicLinkModel.material to a stub MaterialModel populated only with the id from MaterialTopicLink.materialId")
    @Test
    void toTable_buildsMaterialStubFromMaterialId() {
        UUID materialId = UUID.randomUUID();
        MaterialTopicLink domain = MaterialTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(materialId.toString())
                .build();

        MaterialTopicLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getMaterial()).isNotNull();
        assertThat(model.getMaterial().getId()).isEqualTo(materialId);
        assertThat(model.getMaterial().getName()).isNull();
    }

    @DisplayName("Should leave MaterialTopicLinkModel.topic null when MaterialTopicLink.topicId is null")
    @Test
    void toTable_returnsNullTopicWhenTopicIdNull() {
        MaterialTopicLink domain = MaterialTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .topicId(null)
                .build();

        MaterialTopicLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getTopic()).isNull();
    }

    @DisplayName("Should leave MaterialTopicLinkModel.material null when MaterialTopicLink.materialId is null")
    @Test
    void toTable_returnsNullMaterialWhenMaterialIdNull() {
        MaterialTopicLink domain = MaterialTopicLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(null)
                .build();

        MaterialTopicLinkModel model = mapper.toModelEntity(domain);

        assertThat(model.getMaterial()).isNull();
    }

    @DisplayName("Should preserve topicId and materialId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesTopicIdAndMaterialId() {
        UUID topicId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        TopicModel topicModel = TopicModel.builder().id(topicId).build();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        MaterialTopicLinkModel original = MaterialTopicLinkModel.builder()
                .id(UUID.randomUUID())
                .topic(topicModel)
                .material(materialModel)
                .build();

        MaterialTopicLinkModel roundTripped = mapper.toModelEntity(mapper.toDomain(original));

        assertThat(roundTripped.getTopic().getId()).isEqualTo(topicId);
        assertThat(roundTripped.getMaterial().getId()).isEqualTo(materialId);
    }
}
