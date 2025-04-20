package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialTopicAttachmentRepository extends JpaRepository<MaterialTopicAttachment, UUID> {
}
