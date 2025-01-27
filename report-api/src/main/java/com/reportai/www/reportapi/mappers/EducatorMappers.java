package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.entities.Educator;
import java.util.UUID;

public class EducatorMappers {
    private EducatorMappers() {

    }

    public static Educator convert(CreateEducatorDTO createEducatorDTO, UUID id) {
        return Educator
                .builder()
                .name(createEducatorDTO.getName())
                .email(createEducatorDTO.getEmail())
                .tenantId(id.toString())
                .build();
    }

    public static CreateEducatorResponseDTO convert(Educator educator) {
        return CreateEducatorResponseDTO
                .builder()
                .id(educator.getId().toString())
                .name(educator.getName())
                .email(educator.getEmail())
                .build();
    }

}
