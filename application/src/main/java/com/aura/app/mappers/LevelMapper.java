package com.aura.app.mappers;

import com.aura.app.codegen.types.LevelResponse;
import com.aura.core.entities.levels.Level;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LevelMapper {
    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    LevelResponse mapToDto(Level levelTable);

    // MapStruct will use this method to convert Instant -> Long (epoch millis)
    default Long map(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}

