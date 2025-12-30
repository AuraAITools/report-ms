package com.aura.app.mappers;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.core.entities.accounts.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class})
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", source = "createAccountInput.email")
    Account toDomainEntity(CreateAccountInput createAccountInput, String tenantId);


    AccountResponse toDto(Account account);
}
