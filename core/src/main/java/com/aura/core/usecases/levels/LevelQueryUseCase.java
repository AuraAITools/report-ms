package com.aura.core.usecases.levels;

import com.aura.core.entities.levels.Level;
import com.aura.core.usecases.commons.TenantAwareQueryUseCase;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LevelQueryUseCase extends TenantAwareQueryUseCase<Level> {

    @Nullable Level findById(String levelId);

    List<Level> findAllByTenantId(String tenantId);
}