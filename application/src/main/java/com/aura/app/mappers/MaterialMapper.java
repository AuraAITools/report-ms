package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateMaterialInInstitutionInput;
import com.aura.app.codegen.types.MaterialResponse;
import com.aura.core.entities.materials.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface MaterialMapper {

    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Material toDomainEntity(CreateMaterialInInstitutionInput input);

    @Mapping(target = "fileUrl", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    MaterialResponse toDto(Material material);
}
