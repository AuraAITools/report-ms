package com.aura.details.springdata.mappers.institutions;

import com.aura.core.entities.institutions.Institution;
import com.aura.details.springdata.tables.institutions.InstitutionTable;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    Institution toDomainEntity(InstitutionTable institutionTable);

    InstitutionTable toTableEntity(Institution institution);
}

