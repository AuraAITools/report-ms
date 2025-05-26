package com.reportai.www.reportapi.services.attachments;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.repositories.MaterialTopicAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.materials.MaterialsService;
import com.reportai.www.reportapi.services.topics.TopicsService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class MaterialTopicAttachmentService implements ISimpleAttachmentService<Material, Topic> {

    private final MaterialsService materialsService;
    private final TopicsService topicsService;
    private final MaterialTopicAttachmentRepository materialTopicAttachmentRepository;

    @Autowired
    public MaterialTopicAttachmentService(MaterialsService materialsService, TopicsService topicsService, MaterialTopicAttachmentRepository materialTopicAttachmentRepository) {
        this.materialsService = materialsService;
        this.topicsService = topicsService;
        this.materialTopicAttachmentRepository = materialTopicAttachmentRepository;
    }

    @Override
    public ISimpleRead<Material> getEntity1ReadService() {
        return materialsService;
    }

    @Override
    public ISimpleRead<Topic> getEntity2ReadService() {
        return topicsService;
    }

    @Override
    public JpaRepository<MaterialTopicAttachment, UUID> getAttachmentRepository() {
        return materialTopicAttachmentRepository;
    }
}
