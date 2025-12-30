package com.aura.core.repositories;

import com.aura.core.entities.accounts.Account;

public interface AccountRepository {
    Account save(Account account);
}
