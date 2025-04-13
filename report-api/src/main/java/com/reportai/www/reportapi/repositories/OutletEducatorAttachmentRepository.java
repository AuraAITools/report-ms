package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutletEducatorAttachmentRepository extends JpaRepository<OutletEducatorAttachment, UUID>, JpaSpecificationExecutor<OutletEducatorAttachment> {
}