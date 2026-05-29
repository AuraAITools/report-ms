package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.MaterialRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class MaterialQueryUseCaseImpl implements MaterialQueryUseCase {

    private final MaterialRepository materialRepository;

    @Override
    public @Nullable Material findById(String id) {
        return materialRepository.findById(id);
    }

    @Override
    public List<Material> findAllByTenantId(String tenantId) {
        return materialRepository.findAllByTenantId(tenantId);
    }
}
