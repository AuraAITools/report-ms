package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.institutions.dtos.requests.CreateInstitutionDTO;
import com.reportai.www.reportapi.api.v1.institutions.dtos.requests.PatchInstitutionRequestDTO;
import com.reportai.www.reportapi.api.v1.institutions.dtos.responses.InstitutionResponseDto;
import com.reportai.www.reportapi.entities.Institution;

public class InstitutionMappers {
    public static Institution convert(CreateInstitutionDTO from) {
        return Institution.builder()
                .name(from.getName())
                .email(from.getEmail())
                .uen(from.getUen())
                .address(from.getAddress())
                .contactNumber(from.getContactNumber())
                .build();
    }

    public static InstitutionResponseDto convert(Institution from) {
        return InstitutionResponseDto.builder().id(from.getId()).name(from.getName()).email(from.getEmail()).address(from.getAddress()).uen(from.getUen()).contactNumber(from.getContactNumber()).id(from.getId()).build();
    }

    public static Institution convert(PatchInstitutionRequestDTO from, String tenantId) {
        return Institution
                .builder()
                .name(from.getName())
                .email(from.getEmail())
                .uen(from.getUen())
                .address(from.getAddress())
                .contactNumber(from.getContactNumber())
                .tenantId(tenantId)
                .build();
    }

    public static Institution layover(Institution entity, Institution layover) {
        entity.setName(layover.getName());
        entity.setEmail(layover.getEmail());
        entity.setUen(layover.getUen());
        entity.setAddress(layover.getAddress());
        entity.setContactNumber(layover.getContactNumber());
        return entity;
    }

}
