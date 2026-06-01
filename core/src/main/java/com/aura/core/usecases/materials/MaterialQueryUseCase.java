package com.aura.core.usecases.materials;

import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface MaterialQueryUseCase {
    @Nullable Material findById(String id);

    List<Material> findAllByTenantId(String tenantId);

    Map<String, List<Topic>> findTopicsByMaterialIds(List<String> materialIds);
}
