package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorClientResponse;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import java.util.UUID;

public class EducatorMappers {
    private EducatorMappers() {

    }

    public static Educator convert(CreateEducatorDTO createEducatorDTO, UUID id) {
        return Educator
                .builder()
                .employmentType(createEducatorDTO.getEmploymentType())
                .startDate(createEducatorDTO.getStartDate())
                .name(createEducatorDTO.getName())
                .email(createEducatorDTO.getEmail())
                .tenantId(id.toString())
                .build();
    }

    public static CreateEducatorResponseDTO convert(Educator educator) {
        return CreateEducatorResponseDTO
                .builder()
                .levels(educator.getLevels().stream().map(LevelMappers::convert).toList())
                .subjects(educator.getSubjects().stream().map(SubjectMappers::convert).toList())
                .outlets(educator.getOutlets().stream().map(OutletMappers::convert).toList())
                .employmentType(educator.getEmploymentType())
                .id(educator.getId().toString())
                .name(educator.getName())
                .email(educator.getEmail())
                .build();
    }

    public static EducatorClientResponse convert(EducatorClientPersona from) {
        return EducatorClientResponse
                .builder()
                .id(from.getId().toString())
                .firstName(from.getAccount().getFirstName())
                .lastName(from.getAccount().getLastName())
                .email(from.getAccount().getEmail())
                .contact(from.getAccount().getContact())
                .educator(EducatorMappers.convert(from.getEducator()))
                .build();

    }

}
