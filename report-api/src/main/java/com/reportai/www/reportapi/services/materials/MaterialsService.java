package com.reportai.www.reportapi.services.materials;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.repositories.LessonPlanRepository;
import com.reportai.www.reportapi.repositories.MaterialRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.common.utils.AttachmentDelegator;
import com.reportai.www.reportapi.services.topics.TopicsService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MaterialsService implements BaseServiceTemplate<Material, UUID> {
    private final LessonPlanRepository lessonPlanRepository;
    private final MaterialRepository materialRepository;
    private final TopicsService topicsService;
    private final AttachmentDelegator attachmentDelegator;

    @Autowired
    public MaterialsService(MaterialRepository materialRepository, TopicsService topicsService, AttachmentDelegator attachmentDelegator,
                            LessonPlanRepository lessonPlanRepository) {
        this.materialRepository = materialRepository;
        this.topicsService = topicsService;
        this.attachmentDelegator = attachmentDelegator;
        this.lessonPlanRepository = lessonPlanRepository;
    }

    @Override
    public JpaRepository<Material, UUID> getRepository() {
        return materialRepository;
    }

    public Material create(Material material, String file) {
        // TODO: upload to s3 if file exists
        if (file != null) {
            log.info("uploading to s3");
            // save file url
        }

        return materialRepository.save(material);
    }

    public Material attachMaterialToTopic(UUID topicId, UUID materialId) {
        Topic topic = topicsService.findById(topicId);
        Material material = findById(materialId);
        return attachmentDelegator.delegate(topic, material).getMaterial();
    }

    /**
     * TODO: attach material to lesson plan
     */

//    public Material attachMaterialToLessonPlan(UUID lessonPlanId, UUID materialId) {
//        Topic topic = lessonPlanRepository.findById(lessonPlanId);
//        Material material = findById(materialId);
//        return attachmentDelegator.delegate(topic, material).getMaterial();
//    }

}
