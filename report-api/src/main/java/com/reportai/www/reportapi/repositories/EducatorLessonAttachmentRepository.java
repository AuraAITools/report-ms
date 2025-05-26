package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EducatorLessonAttachmentRepository extends JpaRepository<EducatorLessonAttachment, UUID>, JpaSpecificationExecutor<EducatorLessonAttachment> {
    List<EducatorLessonAttachment> findAllByEducator_IdInAndLesson_Id(List<UUID> educatorIds, UUID lessonId);

    @Query("SELECT ela FROM EducatorLessonAttachment ela " +
            "WHERE ela.educator.id = :educatorId " +
            "AND ela.lesson.lessonStartTimestamptz < :endTimestamptz " +
            "AND ela.lesson.lessonEndTimestamptz > :startTimestamptz"
    )
    List<EducatorLessonAttachment> findOverlappingLessonAttachmentsByEducatorId(@Param("educatorId") UUID educatorId, @Param("startTimestamptz") Instant startTimeStampz, @Param("endTimestamptz") Instant endTimeStampz);

    /**
     * Only registers new educators from the incoming educatorIds list
     *
     * @param studentId incoming students
     * @param lessonId
     * @param tenantId
     * @return
     */
    @Modifying
    @Query(value = "INSERT INTO educator_lesson_attachments (id, educator_id, lesson_id, tenant_id, created_at, updated_at) " +
            "SELECT gen_random_uuid(), e.id, :lessonId, :tenantId, NOW(), NOW() " +
            "FROM educators e " +
            "WHERE e.id IN :educatorIds " +
            "AND NOT EXISTS (SELECT 1 FROM educator_lesson_attachments ela " +
            "                WHERE ela.educator_id = e.id AND ela.lesson_id = :lessonId)", nativeQuery = true)
    int registerNewEducatorAttachments(@Param("educatorIds") List<UUID> educatorIds,
                                       @Param("lessonId") UUID lessonId,
                                       @Param("tenantId") String tenantId);

    /**
     * Deletes all educator lesson attachments for a lesson that are not in the incoming educatorIds list
     *
     * @param educatorIds
     * @param lessonId
     * @param tenantId
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM educator_lesson_attachments " +
            "WHERE lesson_id = :lessonId " +
            "AND tenant_id = :tenantId " +
            "AND educator_id NOT IN :educatorIds", nativeQuery = true)
    int deleteEducatorLessonAttachmentsNotIn(@Param("educatorIds") List<UUID> educatorIds,
                                             @Param("lessonId") UUID lessonId,
                                             @Param("tenantId") String tenantId);

    void deleteAllByLesson_Id(UUID lessonId);

    // For a single educator
    List<EducatorLessonAttachment> findAllByEducator_IdAndLesson_Id(UUID educatorId, UUID lessonId);

    // Using consistent property path navigation
    List<EducatorLessonAttachment> findAllByLesson_Id(UUID lessonId);
}
