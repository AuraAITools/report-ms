package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.repositories.MaterialRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class InstitutionMaterialService {
    private final TopicRepository topicRepository;
    private final MaterialRepository materialRepository;

    public InstitutionMaterialService(TopicRepository topicRepository, MaterialRepository materialRepository) {
        this.topicRepository = topicRepository;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public Material createMaterialForTopic(Material material, UUID topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("no topic found"));
        Material savedMaterial = materialRepository.save(material);
        topic.getMaterials().add(savedMaterial);
        topicRepository.save(topic);
        return savedMaterial;
    }

    @Transactional
    public List<Material> batchCreateMaterialForTopic(List<Material> materials, UUID topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("no topic found"));
        List<Material> savedMaterials = materialRepository.saveAll(materials);
        topic.getMaterials().addAll(savedMaterials);
        topicRepository.save(topic);
        return savedMaterials;
    }

    @Transactional
    public List<Material> getAllMaterialsFromTopic(UUID topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("no topic found"));
        return topic.getMaterials();
    }

    @Transactional
    public Material getMaterialFromTopic(UUID materialId) {
        return materialRepository.findById(materialId).orElseThrow(()->new NotFoundException("no material found"));
    }
}
