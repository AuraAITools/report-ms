package com.aura.details.mappers.materials;

import com.aura.core.entities.materials.Material;
import com.aura.details.tables.materials.MaterialTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialMapper {
    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    @Mapping(target = "materialTopicLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonPlanMaterialsLinks", ignore = true)
    Material toDomainEntity(MaterialTable materialTable);

    @Mapping(target = "materialTopicLinks", ignore = true)
    @Mapping(target = "materialLessonLinks", ignore = true)
    @Mapping(target = "lessonPlanMaterialsLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    MaterialTable toTableEntity(Material material);
}

