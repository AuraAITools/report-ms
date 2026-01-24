package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.details.springdata.tables.accounts.AccountTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    Account toDomainEntity(AccountTable accountTable);

    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    AccountTable toTableEntity(Account account);
}

