package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.details.springdata.tables.accounts.AccountTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toDomainEntity(AccountTable accountTable);

    @Mapping(target = "accountStudentLinks", ignore = true)
    @Mapping(target = "accountEducatorLinks", ignore = true)
    @Mapping(target = "Id", source = "id")
    AccountTable toTableEntity(Account account);
}

