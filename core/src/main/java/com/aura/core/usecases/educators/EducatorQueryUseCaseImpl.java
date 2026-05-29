package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.EducatorRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class EducatorQueryUseCaseImpl implements EducatorQueryUseCase {
    private final EducatorRepository educatorRepository;

    @Override
    public @Nullable Educator findById(String id) {
        return educatorRepository.findById(id);
    }

    @Override
    public List<Educator> findAllByTenantId(String tenantId) {
        return educatorRepository.findAllByTenantId(tenantId);
    }
}
