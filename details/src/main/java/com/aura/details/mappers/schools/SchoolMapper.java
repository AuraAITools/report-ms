package com.aura.details.mappers.schools;

import com.aura.core.entities.schools.School;
import com.aura.details.tables.schools.SchoolTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchoolMapper {
    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    @Mapping(target = "students", ignore = true)
    School toDomainEntity(SchoolTable schoolTable);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    SchoolTable toTableEntity(School school);
}

