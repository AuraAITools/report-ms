package com.aura.core.usecases.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.educators.Educator;

public interface CreateEducatorInAccountUseCase {
    Account createEducator(String accountId, Educator educator);
}
