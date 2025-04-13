package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubjectCourseAttachmentRepository extends JpaRepository<SubjectCourseAttachment, UUID>, JpaSpecificationExecutor<SubjectCourseAttachment> {
}