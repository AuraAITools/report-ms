package com.aura.details.springdata.mappers.levels;

import com.aura.core.entities.levels.Level;
import com.aura.details.springdata.tables.levels.LevelTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "students", ignore = true)
    Level toDomainEntity(LevelTable levelTable);

    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    LevelTable toTableEntity(Level level);
}

