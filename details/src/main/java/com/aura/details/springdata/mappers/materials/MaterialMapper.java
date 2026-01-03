package com.aura.details.springdata.mappers.materials;

import com.aura.core.entities.materials.Material;
import com.aura.details.springdata.tables.materials.MaterialTable;
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
    @Mapping(target = "Id", source = "id")
    MaterialTable toTableEntity(Material material);
}

