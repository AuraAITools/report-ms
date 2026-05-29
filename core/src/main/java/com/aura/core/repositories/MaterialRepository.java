package com.aura.core.repositories;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface MaterialRepository {

    Material save(Material material);

    @Nullable Material findById(String id);

    List<Material> findAllByTenantId(String tenantId);

    Map<String, List<Material>> findAllByLessonIds(List<String> lessonIds);

    void deleteById(String id);

    void deleteAllTopicLinksByMaterialId(String materialId);

    void deleteAllLessonPlanLinksByMaterialId(String materialId);

    List<MaterialTopicLink> linkTopicsToMaterial(String materialId, List<String> topicIds, String tenantId);
}
