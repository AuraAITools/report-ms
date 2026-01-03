package com.aura.details.springdata.mappers.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.tables.institutions.InstitutionTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    @Mapping(target = "id", source = "id")
    Institution toDomainEntity(InstitutionTable institutionTable);

    @Mapping(target = "id", source = "id")
    InstitutionTable toTableEntity(Institution institution);
}

