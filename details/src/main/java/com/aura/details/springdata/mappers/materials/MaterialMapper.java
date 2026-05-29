package com.aura.details.springdata.mappers.materials;

import com.aura.core.entities.materials.Material;
import com.aura.details.springdata.models.materials.MaterialModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialMapper {
    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    Material toDomainEntity(MaterialModel materialModel);

    MaterialModel toModelEntity(Material material);
}
