package com.aura.details.springdata.mappers.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.models.institutions.InstitutionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mapping(target = "outlets", ignore = true)
    Institution toDomainEntity(InstitutionModel institutionModel);

    InstitutionModel toModelEntity(Institution institution);
}

