package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanMaterialsLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaMaterialTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.materials.JpaMaterialRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Material repository impl")
@ExtendWith(MockitoExtension.class)
class MaterialRepositoryImplTest {

    @Mock
    private JpaMaterialRepository jpaMaterialRepository;
    @Mock
    private JpaTopicRepository jpaTopicRepository;
    @Mock
    private JpaMaterialTopicLinkRepository jpaMaterialTopicLinkRepository;
    @Mock
    private JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository;
    @InjectMocks
    private MaterialRepositoryImpl repository;

    @DisplayName("Should map domain Material to model, save it via JPA, and map the saved model back to a domain entity")
    @Test
    void save_mapsThroughJpa() {
        UUID id = UUID.randomUUID();
        Material toSave = Material.builder()
                .name("Algebra").tenantId("tenant-1")
                .fileKey("k").description("d").build();
        MaterialModel savedModel = MaterialModel.builder()
                .id(id).name("Algebra").tenantId("tenant-1")
                .fileKey("k").description("d").build();
        when(jpaMaterialRepository.save(any(MaterialModel.class))).thenReturn(savedModel);

        Material result = repository.save(toSave);

        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getName()).isEqualTo("Algebra");
        assertThat(result.getFileKey()).isEqualTo("k");
        assertThat(result.getDescription()).isEqualTo("d");
    }

    @DisplayName("Should return null when findById has no matching row")
    @Test
    void findById_returnsNullWhenAbsent() {
        UUID id = UUID.randomUUID();
        when(jpaMaterialRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(repository.findById(id.toString())).isNull();
    }

    @DisplayName("Should return mapped Material when findById matches")
    @Test
    void findById_returnsMappedDomain() {
        UUID id = UUID.randomUUID();
        MaterialModel model = MaterialModel.builder().id(id).name("Algebra").tenantId("tenant-1").build();
        when(jpaMaterialRepository.findById(id)).thenReturn(Optional.of(model));

        Material result = repository.findById(id.toString());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id.toString());
        assertThat(result.getName()).isEqualTo("Algebra");
    }

    @DisplayName("Should build a link model per topic with the tenantId and material reference, then saveAll")
    @Test
    void linkTopicsToMaterial_buildsAndSavesLinks() {
        UUID materialId = UUID.randomUUID();
        UUID topic1Id = UUID.randomUUID();
        UUID topic2Id = UUID.randomUUID();
        MaterialModel materialRef = MaterialModel.builder().id(materialId).build();
        TopicModel t1 = TopicModel.builder().id(topic1Id).build();
        TopicModel t2 = TopicModel.builder().id(topic2Id).build();

        when(jpaMaterialRepository.getReferenceById(materialId)).thenReturn(materialRef);
        when(jpaTopicRepository.findAllById(List.of(topic1Id, topic2Id))).thenReturn(List.of(t1, t2));

        MaterialTopicLinkModel savedLink1 = MaterialTopicLinkModel.builder()
                .id(UUID.randomUUID()).material(materialRef).topic(t1).tenantId("tenant-1").build();
        MaterialTopicLinkModel savedLink2 = MaterialTopicLinkModel.builder()
                .id(UUID.randomUUID()).material(materialRef).topic(t2).tenantId("tenant-1").build();

        ArgumentCaptor<List<MaterialTopicLinkModel>> captor = ArgumentCaptor.forClass(List.class);
        when(jpaMaterialTopicLinkRepository.saveAll(captor.capture()))
                .thenReturn(List.of(savedLink1, savedLink2));

        List<MaterialTopicLink> result = repository.linkTopicsToMaterial(
                materialId.toString(), List.of(topic1Id.toString(), topic2Id.toString()), "tenant-1");

        assertThat(result).hasSize(2);
        assertThat(captor.getValue()).hasSize(2);
        assertThat(captor.getValue()).allSatisfy(linkModel -> {
            assertThat(linkModel.getMaterial()).isSameAs(materialRef);
            assertThat(linkModel.getTenantId()).isEqualTo("tenant-1");
        });
    }

    @DisplayName("Should return empty list and never hit JPA when topic ID list is empty")
    @Test
    void linkTopicsToMaterial_emptyListReturnsEmpty() {
        UUID materialId = UUID.randomUUID();

        List<MaterialTopicLink> result = repository.linkTopicsToMaterial(
                materialId.toString(), List.of(), "tenant-1");

        assertThat(result).isEmpty();
    }

    @DisplayName("Should group materials by lessonId when querying by lesson ids")
    @Test
    void findAllByLessonIds_groupsByLesson() {
        UUID lesson1 = UUID.randomUUID();
        UUID lesson2 = UUID.randomUUID();
        MaterialModel m1 = MaterialModel.builder().id(UUID.randomUUID()).name("Worksheet").build();
        MaterialModel m2 = MaterialModel.builder().id(UUID.randomUUID()).name("Slides").build();
        MaterialModel m3 = MaterialModel.builder().id(UUID.randomUUID()).name("Video").build();
        when(jpaLessonGroupPlanMaterialsLinkRepository.findMaterialsByLessonIds(List.of(lesson1, lesson2)))
                .thenReturn(List.of(
                        new Object[]{lesson1, m1},
                        new Object[]{lesson1, m2},
                        new Object[]{lesson2, m3}));

        var result = repository.findAllByLessonIds(List.of(lesson1.toString(), lesson2.toString()));

        assertThat(result.get(lesson1.toString())).extracting(Material::getName)
                .containsExactlyInAnyOrder("Worksheet", "Slides");
        assertThat(result.get(lesson2.toString())).extracting(Material::getName)
                .containsExactly("Video");
    }

    @DisplayName("Should return empty map when no lesson ids provided")
    @Test
    void findAllByLessonIds_returnsEmptyForEmptyInput() {
        assertThat(repository.findAllByLessonIds(List.of())).isEmpty();
    }
}
