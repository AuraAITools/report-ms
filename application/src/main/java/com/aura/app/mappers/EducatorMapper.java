package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateEducatorInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.core.entities.educators.Educator;
import com.aura.details.services.AccountService;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, CourseMapper.class, SubjectMapper.class, OutletMapper.class, LevelMapper.class, AccountMapper.class})
public interface EducatorMapper {

    EducatorMapper INSTANCE = Mappers.getMapper(EducatorMapper.class);

    @Mapping(target = "educator", expression = "java(toDomain(input, tenantId))")
    AccountService.CreateEducatorAccountParams toParams(CreateEducatorInput input, String tenantId);

    @Mapping(target = "updatedBy", source = "educator.updatedBy")
    @Mapping(target = "startDate", source = "educator.startDate")
    @Mapping(target = "name", source = "educator.name")
    @Mapping(target = "id", source = "educator.id")
    @Mapping(target = "employmentType", source = "educator.employmentType")
    @Mapping(target = "email", source = "educator.email")
    @Mapping(target = "dateOfBirth", source = "educator.dateOfBirth")
    @Mapping(target = "createdBy", source = "educator.createdBy")
    @Mapping(target = "updatedAt", source = "educator.updatedAt")
    @Mapping(target = "createdAt", source = "educator.createdAt")
    EducatorResponse toDto(AccountService.CreatedEducatorResult result);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "subjectEducatorLinks", ignore = true)
    @Mapping(target = "outletEducatorLinks", ignore = true)
    @Mapping(target = "levelEducatorLinks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "educatorLessonLinks", ignore = true)
    @Mapping(target = "educatorCourseLinks", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    Educator toDomain(CreateEducatorInput input, String tenantId);

}
