package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LevelEducatorAttachmentRepository extends JpaRepository<LevelEducatorAttachment, UUID>, JpaSpecificationExecutor<LevelEducatorAttachment> {
}