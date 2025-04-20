package com.reportai.www.reportapi.services.common.utils;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.repositories.MaterialTopicAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AttachmentDelegator {
    private final MaterialTopicAttachmentRepository materialTopicAttachmentRepository;

    @Autowired
    public AttachmentDelegator(MaterialTopicAttachmentRepository materialTopicAttachmentRepository) {
        this.materialTopicAttachmentRepository = materialTopicAttachmentRepository;
    }

    public MaterialTopicAttachment delegate(Topic topic, Material material) {
        return materialTopicAttachmentRepository.save(MaterialTopicAttachment
                .builder()
                .topic(topic)
                .material(material)
                .build());
    }
}
