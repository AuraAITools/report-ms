package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.MaterialRepository;
import com.aura.details.springdata.mappers.links.MaterialTopicLinkMapper;
import com.aura.details.springdata.mappers.materials.MaterialMapper;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import com.aura.details.springdata.models.materials.MaterialModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.links.JpaLessonGroupPlanMaterialsLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaMaterialTopicLinkRepository;
import com.aura.details.springdata.repositories.jpa.materials.JpaMaterialRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class MaterialRepositoryImpl implements MaterialRepository {

    private final JpaMaterialRepository jpaMaterialRepository;
    private final JpaTopicRepository jpaTopicRepository;
    private final JpaMaterialTopicLinkRepository jpaMaterialTopicLinkRepository;
    private final JpaLessonGroupPlanMaterialsLinkRepository jpaLessonGroupPlanMaterialsLinkRepository;

    @Override
    public Material save(Material material) {
        MaterialModel toCreate = MaterialMapper.INSTANCE.toModelEntity(material);
        MaterialModel created = jpaMaterialRepository.save(toCreate);
        return MaterialMapper.INSTANCE.toDomainEntity(created);
    }

    @Override
    public @Nullable Material findById(String id) {
        return jpaMaterialRepository.findById(UUID.fromString(id))
                .map(MaterialMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<Material> findAllByIds(List<String> ids) {
        if (ids.isEmpty()) return Collections.emptyList();
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        return jpaMaterialRepository.findAllById(uuids).stream()
                .map(MaterialMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Material> findAllByTenantId(String tenantId) {
        return jpaMaterialRepository.findByTenantId(tenantId).stream()
                .map(MaterialMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<Material>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Material>> result = new HashMap<>();
        for (Object[] row : jpaLessonGroupPlanMaterialsLinkRepository.findMaterialsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            MaterialModel materialModel = (MaterialModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(MaterialMapper.INSTANCE.toDomainEntity(materialModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Topic>> findTopicsByMaterialIds(List<String> materialIds) {
        if (materialIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = materialIds.stream().map(UUID::fromString).toList();
        Map<String, List<Topic>> result = new HashMap<>();
        for (Object[] row : jpaMaterialTopicLinkRepository.findTopicsByMaterialIds(uuids)) {
            String materialId = row[0].toString();
            TopicModel topicModel = (TopicModel) row[1];
            result.computeIfAbsent(materialId, k -> new ArrayList<>())
                    .add(TopicMapper.INSTANCE.toDomainEntity(topicModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaMaterialRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllTopicLinksByMaterialId(String materialId) {
        jpaMaterialTopicLinkRepository.deleteAllByMaterialId(UUID.fromString(materialId));
    }

    @Override
    public void deleteAllLessonGroupPlanLinksByMaterialId(String materialId) {
        jpaLessonGroupPlanMaterialsLinkRepository.deleteAllByMaterialId(UUID.fromString(materialId));
    }

    @Override
    public Map<String, List<Material>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
        if (lessonGroupPlanIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonGroupPlanIds.stream().map(UUID::fromString).toList();
        Map<String, List<Material>> result = new HashMap<>();
        for (Object[] row : jpaLessonGroupPlanMaterialsLinkRepository.findMaterialsByLessonGroupPlanIds(uuids)) {
            String planId = row[0].toString();
            MaterialModel materialModel = (MaterialModel) row[1];
            result.computeIfAbsent(planId, k -> new ArrayList<>())
                    .add(MaterialMapper.INSTANCE.toDomainEntity(materialModel));
        }
        return result;
    }

    @Override
    public List<MaterialTopicLink> linkTopicsToMaterial(String materialId, List<String> topicIds, String tenantId) {
        if (topicIds.isEmpty()) return Collections.emptyList();

        MaterialModel materialRef = jpaMaterialRepository.getReferenceById(UUID.fromString(materialId));
        List<UUID> topicUuids = topicIds.stream().map(UUID::fromString).toList();
        List<TopicModel> topicModels = jpaTopicRepository.findAllById(topicUuids);

        List<MaterialTopicLinkModel> linkModels = topicModels.stream()
                .<MaterialTopicLinkModel>map(topic -> MaterialTopicLinkModel.builder()
                        .material(materialRef)
                        .topic(topic)
                        .tenantId(tenantId)
                        .build())
                .toList();

        return jpaMaterialTopicLinkRepository.saveAll(linkModels).stream()
                .map(MaterialTopicLinkMapper.INSTANCE::toDomain)
                .toList();
    }
}
