package com.aura.core.usecases.educators;

import com.aura.core.entities.educators.Educator;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface EducatorQueryUseCase {
    @Nullable Educator findById(String id);

    List<Educator> findAllByTenantId(String tenantId);
}
