package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LessonPlanMaterialLinkMapper {

    LessonPlanMaterialLinkMapper INSTANCE = Mappers.getMapper(LessonPlanMaterialLinkMapper.class);

    @Mapping(source = "lessonPlan.id", target = "lessonPlanId")
    @Mapping(source = "material.id", target = "materialId")
    LessonPlanMaterialLink toDomain(LessonPlanMaterialsLinkModel model);

    @Mapping(source = "lessonPlanId", target = "lessonPlan", qualifiedByName = "lessonPlanRef")
    @Mapping(source = "materialId", target = "material", qualifiedByName = "materialRef")
    LessonPlanMaterialsLinkModel toTable(LessonPlanMaterialLink link);

    @Named("lessonPlanRef")
    default LessonPlanModel lessonPlanRef(String lessonPlanId) {
        if (lessonPlanId == null) {
            return null;
        }
        return LessonPlanModel.builder().id(UUID.fromString(lessonPlanId)).build();
    }

    @Named("materialRef")
    default MaterialModel materialRef(String materialId) {
        if (materialId == null) {
            return null;
        }
        return MaterialModel.builder().id(UUID.fromString(materialId)).build();
    }
}
