package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import com.aura.details.springdata.models.links.LessonGroupPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonGroupPlanMaterialLinkMapper {

    LessonGroupPlanMaterialLinkMapper INSTANCE = Mappers.getMapper(LessonGroupPlanMaterialLinkMapper.class);

    @Mapping(source = "lessonGroupPlan.id", target = "lessonGroupPlanId")
    @Mapping(source = "material.id", target = "materialId")
    LessonGroupPlanMaterialLink toDomain(LessonGroupPlanMaterialsLinkModel model);

    @Mapping(source = "lessonGroupPlanId", target = "lessonGroupPlan", qualifiedByName = "lessonGroupPlanRef")
    @Mapping(source = "materialId", target = "material", qualifiedByName = "materialRef")
    LessonGroupPlanMaterialsLinkModel toTable(LessonGroupPlanMaterialLink link);

    @Named("lessonGroupPlanRef")
    default LessonGroupPlanModel lessonGroupPlanRef(String lessonGroupPlanId) {
        if (lessonGroupPlanId == null) {
            return null;
        }
        return LessonGroupPlanModel.builder().id(UUID.fromString(lessonGroupPlanId)).build();
    }

    @Named("materialRef")
    default MaterialModel materialRef(String materialId) {
        if (materialId == null) {
            return null;
        }
        return MaterialModel.builder().id(UUID.fromString(materialId)).build();
    }
}
