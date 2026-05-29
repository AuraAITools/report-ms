package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateSchoolInput;
import com.aura.app.codegen.types.SchoolResponse;
import com.aura.core.entities.schools.School;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface SchoolMapper {
    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    SchoolResponse toDto(School school);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    School toDomain(CreateSchoolInput input);
}
