package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.tables.accounts.AccountTable;

public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;

    public AccountRepositoryImpl(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public Account save(Account account) {
        AccountTable accountTable = AccountMapper.INSTANCE.toTableEntity(account);
        AccountTable savedAccountTable = jpaAccountRepository.save(accountTable);
        return AccountMapper.INSTANCE.toDomainEntity(savedAccountTable);
    }
}
