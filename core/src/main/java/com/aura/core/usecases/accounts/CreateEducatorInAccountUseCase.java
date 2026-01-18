package com.aura.core.usecases.accounts;

import com.aura.core.entities.educators.Educator;

public interface CreateEducatorInAccountUseCase {
    Educator createEducator(String accountId, Educator educator);
}
