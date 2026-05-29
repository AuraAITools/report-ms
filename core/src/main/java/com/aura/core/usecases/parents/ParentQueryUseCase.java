package com.aura.core.usecases.parents;

import com.aura.core.entities.accounts.Parent;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface ParentQueryUseCase {
    @Nullable Parent findById(String id);

    List<Parent> findAllByTenantId(String tenantId);
}
