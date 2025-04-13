package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorClientResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorResponseDTO;
import com.reportai.www.reportapi.entities.educators.Educator;
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

    public static EducatorResponseDTO convert(Educator educator) {
        return EducatorResponseDTO
                .builder()
                .levels(educator.getLevelEducatorAttachments() == null ? null : educator.getLevelEducatorAttachments()
                        .stream()
                        .map(
                                levelEducatorAttachment -> LevelMappers.convert(levelEducatorAttachment.getLevel()))
                        .toList())
                .subjects(educator.getSubjectEducatorAttachments() == null ? null : educator.getSubjectEducatorAttachments().stream().map(subjectEducatorAttachment -> SubjectMappers.convert(subjectEducatorAttachment.getSubject())).toList())
                .outlets(educator.getOutletEducatorAttachments() == null ? null : educator.getOutletEducatorAttachments().stream().map(outletEducatorAttachment -> OutletMappers.convert(outletEducatorAttachment.getOutlet())).toList())
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
