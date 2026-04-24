package com.aura.details.springdata.mappers.levels;

import com.aura.core.entities.levels.Level;
import com.aura.details.springdata.models.levels.LevelModel;
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
    Level toDomainEntity(LevelModel levelModel);

    @Mapping(target = "subjectLevelLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "students", ignore = true)
    LevelModel toModelEntity(Level level);
}

