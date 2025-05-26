package com.reportai.www.reportapi.mappers;

import com.reportai.www.reportapi.api.v1.institutions.requests.PatchInstitutionRequestDTO;
import com.reportai.www.reportapi.entities.Institution;
import java.util.UUID;

public class InstitutionMappers {

    public static Institution convert(PatchInstitutionRequestDTO from, String institutionId) {
        return Institution
                .builder()
                .Id(UUID.fromString(institutionId))
                .name(from.getName())
                .email(from.getEmail())
                .uen(from.getUen())
                .address(from.getAddress())
                .contactNumber(from.getContactNumber())
                .build();
    }

    public static Institution layover(Institution entity, Institution layover) {
        entity.setName(layover.getName());
        entity.setEmail(layover.getEmail());
        entity.setUen(layover.getUen());
        entity.setAddress(layover.getAddress());
        entity.setContactNumber(layover.getContactNumber());
        entity.setId(layover.getId());
        return entity;
    }

}
