package com.reportai.www.reportapi.services.materials;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.repositories.MaterialRepository;
import com.reportai.www.reportapi.services.attachments.MaterialTopicAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MaterialsService implements ISimpleRead<Material> {
    private final MaterialRepository materialRepository;
    MaterialTopicAttachmentService materialTopicAttachmentService;

    @Autowired
    public MaterialsService(MaterialRepository materialRepository, @Lazy MaterialTopicAttachmentService materialTopicAttachmentService) {
        this.materialRepository = materialRepository;
        this.materialTopicAttachmentService = materialTopicAttachmentService;
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

    public MaterialTopicAttachment attachMaterialToTopic(UUID materialId, UUID topicID) {
        return materialTopicAttachmentService.attach(materialId, topicID, new MaterialTopicAttachment());
    }

    /**
     * TODO: attach material to lesson plan
     */

}
