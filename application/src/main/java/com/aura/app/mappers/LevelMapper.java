package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateLevelInInstitutionInput;
import com.aura.app.codegen.types.LevelResponse;
import com.aura.core.entities.levels.Level;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateToEpochMapper.class)
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    LevelResponse mapToDto(Level levelModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "institutionId", target = "tenantId")
    Level toDomainEntity(CreateLevelInInstitutionInput input);
}

