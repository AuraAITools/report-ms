package com.aura.app.mappers;

import com.aura.app.codegen.types.InstitutionCreationRequestResponse;
import com.aura.app.codegen.types.RequestInstitutionCreationInput;
import com.aura.core.entities.institutions.InstitutionCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface InstitutionCreationRequestMapper {
    InstitutionCreationRequestMapper INSTANCE = Mappers.getMapper(InstitutionCreationRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "institutionName", source = "createInstitutionInput.name")
    @Mapping(target = "institutionEmail", source = "createInstitutionInput.email")
    @Mapping(target = "institutionContactNumber", source = "createInstitutionInput.contactNumber")
    @Mapping(target = "institutionAddress", source = "createInstitutionInput.address")
    @Mapping(target = "institutionUen", source = "createInstitutionInput.uen")
    InstitutionCreationRequest from(RequestInstitutionCreationInput input);

    InstitutionCreationRequestResponse from(InstitutionCreationRequest request);
}
