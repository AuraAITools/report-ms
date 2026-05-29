package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface MaterialQueryUseCase {
    @Nullable Material findById(String id);

    List<Material> findAllByTenantId(String tenantId);
}
