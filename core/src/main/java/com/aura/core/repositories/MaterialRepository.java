package com.aura.core.repositories;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface MaterialRepository {

    Material save(Material material);

    @Nullable Material findById(String id);

    List<Material> findAllByTenantId(String tenantId);

    Map<String, List<Material>> findAllByLessonIds(List<String> lessonIds);

    Map<String, List<Topic>> findTopicsByMaterialIds(List<String> materialIds);

    void deleteById(String id);

    void deleteAllTopicLinksByMaterialId(String materialId);

    void deleteAllLessonGroupPlanLinksByMaterialId(String materialId);

    Map<String, List<Material>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds);

    List<MaterialTopicLink> linkTopicsToMaterial(String materialId, List<String> topicIds, String tenantId);
}
