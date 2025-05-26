package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EducatorCourseAttachmentRepository extends JpaRepository<EducatorCourseAttachment, UUID>, JpaSpecificationExecutor<EducatorCourseAttachment> {
}