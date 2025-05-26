package com.reportai.www.reportapi.entities.attachments;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountEducatorAttachmentRepository extends JpaRepository<AccountEducatorAttachment, UUID>, JpaSpecificationExecutor<AccountEducatorAttachment> {
}