package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.MaterialRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateMaterialInInstitutionUseCaseImpl implements CreateMaterialInInstitutionUseCase {

    private final MaterialRepository materialRepository;

    @Override
    public Material create(Material material, List<String> topicIds) {
        Material created = materialRepository.save(material);
        if (!topicIds.isEmpty()) {
            materialRepository.linkTopicsToMaterial(created.getId(), topicIds, created.getTenantId());
        }
        return created;
    }
}
