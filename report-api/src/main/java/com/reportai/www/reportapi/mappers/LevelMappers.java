package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsRequestDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import java.util.UUID;

public class LevelMappers {
    private LevelMappers() {
    }

    public static Level convert(UUID id, CreateLevelsRequestDTO createLevelsRequestDTO) {
        return Level
                .builder()
                .name(createLevelsRequestDTO.getName())
                .tenantId(id.toString())
                .build();
    }

    public static CreateLevelsResponseDTO convert(Level from) {
        return CreateLevelsResponseDTO
                .builder()
                .id(from.getId().toString())
                .name(from.getName())
                .build();
    }
}
