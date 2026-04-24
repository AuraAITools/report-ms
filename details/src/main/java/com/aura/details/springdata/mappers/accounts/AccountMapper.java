package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.details.springdata.models.accounts.AccountModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    Account toDomainEntity(AccountModel accountModel);

    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    AccountModel toModelEntity(Account account);
}

