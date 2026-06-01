package com.aura.core.usecases.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.students.Student;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface AccountQueryUseCase {
    @Nullable Account findById(String id);

    @Nullable Account findByUserIdAndTenantId(String userId, String tenantId);

    List<Account> findAllByTenantId(String tenantId);

    Map<String, List<Student>> findStudentsByAccountIds(List<String> accountIds);

    Map<String, List<Educator>> findEducatorsByAccountIds(List<String> accountIds);

    Map<String, Parent> findParentsByAccountIds(List<String> accountIds);
}
