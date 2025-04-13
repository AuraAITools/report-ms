package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubjectEducatorAttachmentRepository extends JpaRepository<SubjectEducatorAttachment, UUID>, JpaSpecificationExecutor<SubjectEducatorAttachment> {
}