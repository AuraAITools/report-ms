package com.aura.app.mappers;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
import com.aura.core.entities.accounts.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "relationship", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", source = "createAccountInput.email")
    Account toDomainEntity(CreateInstitutionAdminAccountInput createAccountInput, String tenantId);


    @Mapping(target = "version", ignore = true)
    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", source = "createParentAccountInput.email")
    Account toDomainEntity(CreateParentAccountInput createParentAccountInput, String tenantId);


    AccountResponse toDto(Account account);
}
