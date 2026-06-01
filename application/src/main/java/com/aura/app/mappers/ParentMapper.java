package com.aura.app.mappers;

import com.aura.app.codegen.types.CreateParentAndStudentsInAccountInput;
import com.aura.app.codegen.types.CreateParentInAccountInput;
import com.aura.app.codegen.types.ParentResponse;
import com.aura.core.entities.accounts.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface ParentMapper {
    ParentMapper INSTANCE = Mappers.getMapper(ParentMapper.class);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    Parent toDomainEntity(CreateParentInAccountInput input);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tenantId", source = "institutionId")
    Parent toDomainEntity(CreateParentAndStudentsInAccountInput input);

    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    ParentResponse toDto(Parent parent);
}
