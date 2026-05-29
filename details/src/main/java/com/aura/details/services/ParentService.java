package com.aura.details.services;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.accounts.Parent.RELATIONSHIP;
import com.aura.core.usecases.parents.DeleteParentUseCase;
import com.aura.core.usecases.parents.ParentQueryUseCase;
import com.aura.core.usecases.parents.UpdateParentUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class ParentService {
    private final ParentQueryUseCase parentQueryUseCase;
    private final UpdateParentUseCase updateParentUseCase;
    private final DeleteParentUseCase deleteParentUseCase;

    public @Nullable Parent findById(String id) {
        return parentQueryUseCase.findById(id);
    }

    public List<Parent> findAllByTenantId(String tenantId) {
        return parentQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public Parent updateById(String id, @Nullable RELATIONSHIP relationship) {
        return updateParentUseCase.update(id, relationship);
    }

    @Transactional
    public void deleteById(String id) {
        deleteParentUseCase.deleteById(id);
    }
}
