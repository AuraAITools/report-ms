package com.aura.core.usecases.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.ParentRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class AccountQueryUseCaseImpl implements AccountQueryUseCase {
    private final AccountRepository accountRepository;
    private final ParentRepository parentRepository;

    @Override
    public @Nullable Account findById(String id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> findAllByTenantId(String tenantId) {
        return accountRepository.findAllByTenantId(tenantId);
    }

    @Override
    public Map<String, List<Student>> findStudentsByAccountIds(List<String> accountIds) {
        return accountRepository.findStudentsByAccountIds(accountIds);
    }

    @Override
    public Map<String, List<Educator>> findEducatorsByAccountIds(List<String> accountIds) {
        return accountRepository.findEducatorsByAccountIds(accountIds);
    }

    @Override
    public Map<String, Parent> findParentsByAccountIds(List<String> accountIds) {
        return parentRepository.findAllByAccountIds(accountIds);
    }
}
