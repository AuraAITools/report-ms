package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateInstitutionInput;
import com.aura.app.codegen.types.InstitutionResponse;
import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.models.institutions.InstitutionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mapping(target = "outlets", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Institution from(CreateInstitutionInput createInstitutionInput);

    @Mapping(target = "outlets", ignore = true)
    InstitutionResponse from(Institution institution);

    @Mapping(target = "outlets", ignore = true)
    InstitutionResponse from(InstitutionModel institutionModel);

}
