package com.aura.details.springdata.mappers.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.details.springdata.tables.educators.EducatorTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EducatorMapper {
    EducatorMapper INSTANCE = Mappers.getMapper(EducatorMapper.class);

    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    Educator toDomainEntity(EducatorTable educatorTable);

    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "Id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    EducatorTable toTableEntity(Educator educator);
}

