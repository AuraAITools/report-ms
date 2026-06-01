package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateEducatorInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.core.entities.educators.Educator;
import com.aura.details.services.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, CourseMapper.class, SubjectMapper.class, OutletMapper.class, LevelMapper.class, AccountMapper.class, AccountMinimalDetailMapper.class})
public interface EducatorMapper {

    EducatorMapper INSTANCE = Mappers.getMapper(EducatorMapper.class);

    @Mapping(target = "educator", expression = "java(toDomain(input))")
    AccountService.CreateEducatorAccountParams toParams(CreateEducatorInput input);

    @Mapping(target = "updatedBy", source = "educator.updatedBy", qualifiedByName = "toMinimal")
    @Mapping(target = "startDate", source = "educator.startDate")
    @Mapping(target = "name", source = "educator.name")
    @Mapping(target = "id", source = "educator.id")
    @Mapping(target = "employmentType", source = "educator.employmentType")
    @Mapping(target = "email", source = "educator.email")
    @Mapping(target = "dateOfBirth", source = "educator.dateOfBirth")
    @Mapping(target = "createdBy", source = "educator.createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedAt", source = "educator.updatedAt")
    @Mapping(target = "createdAt", source = "educator.createdAt")
    @Mapping(target = "profileImageUrl", ignore = true)
    EducatorResponse toDto(AccountService.CreatedEducatorResult result);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    Educator toDomain(CreateEducatorInput input);

    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "outlets", ignore = true)
    @Mapping(target = "levels", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    EducatorResponse toDto(Educator educator);

}
