package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorClientResponseDTO;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import java.util.UUID;

public class EducatorMappers {
    private EducatorMappers() {

    }

    public static Educator convert(CreateEducatorRequestDTO createEducatorRequestDTO, UUID id) {
        return Educator
                .builder()
                .employmentType(createEducatorRequestDTO.getEmploymentType())
                .startDate(createEducatorRequestDTO.getStartDate())
                .name(createEducatorRequestDTO.getName())
                .dateOfBirth(createEducatorRequestDTO.getDateOfBirth())
                .email(createEducatorRequestDTO.getEmail())
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

    public static EducatorClientResponseDTO convert(EducatorClientPersona from) {
        return EducatorClientResponseDTO
                .builder()
                .id(from.getId().toString())
                .firstName(from.getAccount().getFirstName())
                .lastName(from.getAccount().getLastName())
                .email(from.getAccount().getEmail())
                .contact(from.getAccount().getContact())
                .educator(from.getEducator() == null ? null : EducatorMappers.convert(from.getEducator()))
                .build();

    }

}
