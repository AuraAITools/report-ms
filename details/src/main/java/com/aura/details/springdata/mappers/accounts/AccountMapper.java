package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.details.springdata.models.accounts.AccountModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ParentMapper.class)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toDomainEntity(AccountModel accountModel);

    AccountModel toModelEntity(Account account);
}

