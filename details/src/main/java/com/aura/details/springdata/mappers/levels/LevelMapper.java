package com.aura.details.springdata.mappers.levels;

import com.aura.core.entities.levels.Level;
import com.aura.details.springdata.models.levels.LevelModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    Level toDomainEntity(LevelModel levelModel);

    LevelModel toModelEntity(Level level);
}
