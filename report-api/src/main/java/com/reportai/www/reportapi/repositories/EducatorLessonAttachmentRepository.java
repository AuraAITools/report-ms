package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EducatorLessonAttachmentRepository extends JpaRepository<EducatorLessonAttachment, UUID>, JpaSpecificationExecutor<EducatorLessonAttachment> {
    List<EducatorLessonAttachment> findAllByEducator_IdInAndLesson_Id(List<UUID> educatorIds, UUID lessonId);

    // For a single educator
    List<EducatorLessonAttachment> findAllByEducator_IdAndLesson_Id(UUID educatorId, UUID lessonId);

    // Using consistent property path navigation
    List<EducatorLessonAttachment> findAllByLesson_Id(UUID lessonId);
}
