package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EducatorLessonAttachmentRepository extends JpaRepository<EducatorLessonAttachment, UUID>, JpaSpecificationExecutor<EducatorLessonAttachment> {
    List<EducatorLessonAttachment> findAllByEducator_IdInAndLesson_Id(List<UUID> educatorIds, UUID lessonId);

    @Query("SELECT ela FROM EducatorLessonAttachment ela " +
            "WHERE ela.educator.id = :educatorId " +
            "AND ela.lesson.lessonStartTimestamptz < :endTimestamptz " +
            "AND ela.lesson.lessonEndTimestamptz > :startTimestamptz"
    )
    List<EducatorLessonAttachment> findOverlappingLessonAttachmentsByEducatorId(@Param("educatorId") UUID educatorId, @Param("startTimestamptz") Instant startTimeStampz, @Param("endTimeStamptz") Instant endTimeStampz);

    // For a single educator
    List<EducatorLessonAttachment> findAllByEducator_IdAndLesson_Id(UUID educatorId, UUID lessonId);

    // Using consistent property path navigation
    List<EducatorLessonAttachment> findAllByLesson_Id(UUID lessonId);
}
