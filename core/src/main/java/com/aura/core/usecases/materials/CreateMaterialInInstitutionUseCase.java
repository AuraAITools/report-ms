package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import java.util.List;

public interface CreateMaterialInInstitutionUseCase {
    Material create(Material material, List<String> topicIds);
}
