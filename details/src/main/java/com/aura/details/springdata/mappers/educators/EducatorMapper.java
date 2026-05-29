package com.aura.details.springdata.mappers.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.details.springdata.models.educators.EducatorModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducatorMapper {
    EducatorMapper INSTANCE = Mappers.getMapper(EducatorMapper.class);

    Educator toDomainEntity(EducatorModel table);

    EducatorModel toModelEntity(Educator domain);
}
