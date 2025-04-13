package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubjectLevelAttachmentRepository extends JpaRepository<SubjectLevelAttachment, UUID>, JpaSpecificationExecutor<SubjectLevelAttachment> {
}