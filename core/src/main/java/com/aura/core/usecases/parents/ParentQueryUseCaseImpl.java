package com.aura.core.usecases.parents;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.repositories.ParentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class ParentQueryUseCaseImpl implements ParentQueryUseCase {
    private final ParentRepository parentRepository;

    @Override
    public @Nullable Parent findById(String id) {
        return parentRepository.findById(id);
    }

    @Override
    public List<Parent> findAllByTenantId(String tenantId) {
        return parentRepository.findAllByTenantId(tenantId);
    }
}
