package com.aura.details.springdata.mappers.schools;

import com.aura.core.entities.schools.School;
import com.aura.details.springdata.models.schools.SchoolModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchoolMapper {
    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    School toDomainEntity(SchoolModel schoolModel);

    SchoolModel toModelEntity(School school);
}
