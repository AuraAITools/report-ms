package com.aura.app.http.rest.v1.institutions.mappers;

import com.aura.app.http.rest.v1.institutions.requests.CreateInstitutionRequestDTO;
import com.aura.app.http.rest.v1.institutions.responses.InstitutionResponseDTO;
import com.aura.core.entities.institutions.Institution;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppInstitutionMapper {
    AppInstitutionMapper INSTANCE = Mappers.getMapper(AppInstitutionMapper.class);

    default Institution from(CreateInstitutionRequestDTO dto) {
        // Generate shared UUID
        UUID institutionId = UUID.randomUUID();

        return Institution.builder()
                .id(institutionId)
                .name(dto.getName())
                .email(dto.getEmail())
                .uen(dto.getUen())
                .address(dto.getAddress())
                .contactNumber(dto.getContactNumber())
                .tenantId(institutionId.toString())
                .build();
    }

    InstitutionResponseDTO from(Institution institution);
}
