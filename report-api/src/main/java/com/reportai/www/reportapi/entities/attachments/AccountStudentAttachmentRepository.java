package com.reportai.www.reportapi.entities.attachments;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountStudentAttachmentRepository extends JpaRepository<AccountStudentAttachment, UUID>, JpaSpecificationExecutor<AccountStudentAttachment> {
}