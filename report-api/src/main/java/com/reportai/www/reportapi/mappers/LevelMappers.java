package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.levels.requests.CreateLevelsDTO;
import com.reportai.www.reportapi.api.v1.levels.responses.CreateLevelsResponseDTO;
import com.reportai.www.reportapi.entities.Level;
import java.util.UUID;

public class LevelMappers {
    private LevelMappers() {
    }

    public static Level convert(UUID id, CreateLevelsDTO createLevelsDTO) {
        return Level
                .builder()
                .name(createLevelsDTO.getName())
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
