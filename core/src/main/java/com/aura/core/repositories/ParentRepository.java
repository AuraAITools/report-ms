package com.aura.core.repositories;

import com.aura.core.entities.accounts.Parent;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface ParentRepository {
    Parent save(Parent parent);

    List<Parent> saveAll(List<Parent> parents);

    @Nullable Parent findById(String id);

    Map<String, Parent> findAllByAccountIds(List<String> accountIds);

    List<Parent> findAllByTenantId(String tenantId);

    void deleteById(String id);
}
