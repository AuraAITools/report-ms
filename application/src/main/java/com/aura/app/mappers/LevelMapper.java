package com.aura.app.mappers;

import com.aura.app.codegen.types.LevelResponse;
import com.aura.core.entities.levels.Level;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateToEpochMapper.class)
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    LevelResponse mapToDto(Level levelTable);

}

