package com.aura.details.springdata.mappers.institutions;

import com.aura.core.entities.institutions.InstitutionCreationRequest;
import com.aura.details.springdata.models.institutions.InstitutionCreationRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionCreationRequestMapper {
    InstitutionCreationRequestMapper INSTANCE = Mappers.getMapper(InstitutionCreationRequestMapper.class);

    InstitutionCreationRequest toDomainEntity(InstitutionCreationRequestModel model);

    InstitutionCreationRequestModel toModelEntity(InstitutionCreationRequest request);
}
