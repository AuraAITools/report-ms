package com.aura.details.springdata.mappers.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.details.springdata.models.subjects.SubjectModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    Subject toDomainEntity(SubjectModel subjectModel);

    SubjectModel toModelEntity(Subject subject);
}
