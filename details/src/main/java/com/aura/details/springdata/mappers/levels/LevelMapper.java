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
    LevelTable toTableEntity(Level level);
}

