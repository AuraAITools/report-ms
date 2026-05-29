package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonPlanMaterialLinkMapper")
class LessonPlanMaterialLinkMapperTest {

    private final LessonPlanMaterialLinkMapper mapper = LessonPlanMaterialLinkMapper.INSTANCE;

    @DisplayName("Should map LessonPlanMaterialsLinkModel.lessonPlan.id into LessonPlanMaterialLink.lessonPlanId without traversing back into LessonPlan")
    @Test
    void toDomain_extractsLessonPlanIdFromModel() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        LessonPlanMaterialsLinkModel model = LessonPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(lessonPlanModel)
                .build();

        LessonPlanMaterialLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonPlanId()).isEqualTo(lessonPlanId.toString());
    }

    @DisplayName("Should map LessonPlanMaterialsLinkModel.material.id into LessonPlanMaterialLink.materialId without traversing back into Material")
    @Test
    void toDomain_extractsMaterialIdFromModel() {
        UUID materialId = UUID.randomUUID();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        LessonPlanMaterialsLinkModel model = LessonPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .material(materialModel)
                .build();

        LessonPlanMaterialLink domain = mapper.toDomain(model);

        assertThat(domain.getMaterialId()).isEqualTo(materialId.toString());
    }

    @DisplayName("Should set LessonPlanMaterialsLinkModel.lessonPlan to a stub LessonPlanModel populated only with the id from LessonPlanMaterialLink.lessonPlanId")
    @Test
    void toTable_buildsLessonPlanStubFromLessonPlanId() {
        UUID lessonPlanId = UUID.randomUUID();
        LessonPlanMaterialLink domain = LessonPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(lessonPlanId.toString())
                .build();

        LessonPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonPlan()).isNotNull();
        assertThat(model.getLessonPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(model.getLessonPlan().getLesson()).isNull();
    }

    @DisplayName("Should set LessonPlanMaterialsLinkModel.material to a stub MaterialModel populated only with the id from LessonPlanMaterialLink.materialId")
    @Test
    void toTable_buildsMaterialStubFromMaterialId() {
        UUID materialId = UUID.randomUUID();
        LessonPlanMaterialLink domain = LessonPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(materialId.toString())
                .build();

        LessonPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getMaterial()).isNotNull();
        assertThat(model.getMaterial().getId()).isEqualTo(materialId);
        assertThat(model.getMaterial().getName()).isNull();
    }

    @DisplayName("Should leave LessonPlanMaterialsLinkModel.lessonPlan null when LessonPlanMaterialLink.lessonPlanId is null")
    @Test
    void toTable_returnsNullLessonPlanWhenLessonPlanIdNull() {
        LessonPlanMaterialLink domain = LessonPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonPlanId(null)
                .build();

        LessonPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonPlan()).isNull();
    }

    @DisplayName("Should leave LessonPlanMaterialsLinkModel.material null when LessonPlanMaterialLink.materialId is null")
    @Test
    void toTable_returnsNullMaterialWhenMaterialIdNull() {
        LessonPlanMaterialLink domain = LessonPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(null)
                .build();

        LessonPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getMaterial()).isNull();
    }

    @DisplayName("Should preserve lessonPlanId and materialId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonPlanIdAndMaterialId() {
        UUID lessonPlanId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        LessonPlanModel lessonPlanModel = LessonPlanModel.builder().id(lessonPlanId).build();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        LessonPlanMaterialsLinkModel original = LessonPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonPlan(lessonPlanModel)
                .material(materialModel)
                .build();

        LessonPlanMaterialsLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getLessonPlan().getId()).isEqualTo(lessonPlanId);
        assertThat(roundTripped.getMaterial().getId()).isEqualTo(materialId);
    }
}
