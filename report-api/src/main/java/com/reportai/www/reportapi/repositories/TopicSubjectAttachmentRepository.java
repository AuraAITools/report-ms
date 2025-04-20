package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.TopicSubjectAttachment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicSubjectAttachmentRepository extends JpaRepository<TopicSubjectAttachment, UUID> {
}