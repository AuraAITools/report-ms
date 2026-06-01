package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.links.LessonGroupPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LessonGroupPlanMaterialLinkMapper")
class LessonPlanMaterialLinkMapperTest {

    private final LessonGroupPlanMaterialLinkMapper mapper = LessonGroupPlanMaterialLinkMapper.INSTANCE;

    @DisplayName("Should map LessonGroupPlanMaterialsLinkModel.lessonGroupPlan.id into LessonGroupPlanMaterialLink.lessonGroupPlanId without traversing back into LessonGroupPlan")
    @Test
    void toDomain_extractsLessonGroupPlanIdFromModel() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        LessonGroupPlanMaterialsLinkModel model = LessonGroupPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .build();

        LessonGroupPlanMaterialLink domain = mapper.toDomain(model);

        assertThat(domain.getLessonGroupPlanId()).isEqualTo(lessonGroupPlanId.toString());
    }

    @DisplayName("Should map LessonGroupPlanMaterialsLinkModel.material.id into LessonGroupPlanMaterialLink.materialId without traversing back into Material")
    @Test
    void toDomain_extractsMaterialIdFromModel() {
        UUID materialId = UUID.randomUUID();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        LessonGroupPlanMaterialsLinkModel model = LessonGroupPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .material(materialModel)
                .build();

        LessonGroupPlanMaterialLink domain = mapper.toDomain(model);

        assertThat(domain.getMaterialId()).isEqualTo(materialId.toString());
    }

    @DisplayName("Should set LessonGroupPlanMaterialsLinkModel.lessonGroupPlan to a stub LessonGroupPlanModel populated only with the id from LessonGroupPlanMaterialLink.lessonGroupPlanId")
    @Test
    void toTable_buildsLessonGroupPlanStubFromLessonGroupPlanId() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        LessonGroupPlanMaterialLink domain = LessonGroupPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(lessonGroupPlanId.toString())
                .build();

        LessonGroupPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonGroupPlan()).isNotNull();
        assertThat(model.getLessonGroupPlan().getId()).isEqualTo(lessonGroupPlanId);
        assertThat(model.getLessonGroupPlan().getLessonGroup()).isNull();
    }

    @DisplayName("Should set LessonGroupPlanMaterialsLinkModel.material to a stub MaterialModel populated only with the id from LessonGroupPlanMaterialLink.materialId")
    @Test
    void toTable_buildsMaterialStubFromMaterialId() {
        UUID materialId = UUID.randomUUID();
        LessonGroupPlanMaterialLink domain = LessonGroupPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(materialId.toString())
                .build();

        LessonGroupPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getMaterial()).isNotNull();
        assertThat(model.getMaterial().getId()).isEqualTo(materialId);
        assertThat(model.getMaterial().getName()).isNull();
    }

    @DisplayName("Should leave LessonGroupPlanMaterialsLinkModel.lessonGroupPlan null when LessonGroupPlanMaterialLink.lessonGroupPlanId is null")
    @Test
    void toTable_returnsNullLessonGroupPlanWhenLessonGroupPlanIdNull() {
        LessonGroupPlanMaterialLink domain = LessonGroupPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(null)
                .build();

        LessonGroupPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getLessonGroupPlan()).isNull();
    }

    @DisplayName("Should leave LessonGroupPlanMaterialsLinkModel.material null when LessonGroupPlanMaterialLink.materialId is null")
    @Test
    void toTable_returnsNullMaterialWhenMaterialIdNull() {
        LessonGroupPlanMaterialLink domain = LessonGroupPlanMaterialLink.builder()
                .id(UUID.randomUUID().toString())
                .materialId(null)
                .build();

        LessonGroupPlanMaterialsLinkModel model = mapper.toTable(domain);

        assertThat(model.getMaterial()).isNull();
    }

    @DisplayName("Should preserve lessonGroupPlanId and materialId across a Model→Domain→Model round-trip")
    @Test
    void roundTrip_preservesLessonGroupPlanIdAndMaterialId() {
        UUID lessonGroupPlanId = UUID.randomUUID();
        UUID materialId = UUID.randomUUID();
        LessonGroupPlanModel lessonGroupPlanModel = LessonGroupPlanModel.builder().id(lessonGroupPlanId).build();
        MaterialModel materialModel = MaterialModel.builder().id(materialId).build();
        LessonGroupPlanMaterialsLinkModel original = LessonGroupPlanMaterialsLinkModel.builder()
                .id(UUID.randomUUID())
                .lessonGroupPlan(lessonGroupPlanModel)
                .material(materialModel)
                .build();

        LessonGroupPlanMaterialsLinkModel roundTripped = mapper.toTable(mapper.toDomain(original));

        assertThat(roundTripped.getLessonGroupPlan().getId()).isEqualTo(lessonGroupPlanId);
        assertThat(roundTripped.getMaterial().getId()).isEqualTo(materialId);
    }
}
