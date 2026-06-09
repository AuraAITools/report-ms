package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorLessonLinkRepository extends JpaRepository<EducatorLessonLinkModel, UUID>, JpaSpecificationExecutor<EducatorLessonLinkModel> {

    @Query("SELECT l.lesson.id, l.educator FROM EducatorLessonLinkModel l WHERE l.lesson.id IN :lessonIds")
    List<Object[]> findEducatorsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT l.lesson FROM EducatorLessonLinkModel l WHERE l.educator.id = :educatorId")
    List<LessonModel> findLessonsByEducatorId(@Param("educatorId") UUID educatorId);

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllByEducatorId(UUID educatorId);

    @Query("""
            SELECT l.lesson FROM EducatorLessonLinkModel l
            WHERE l.tenantId = :tenantId
              AND l.educator.id = :educatorId
              AND l.lesson.state <> com.aura.details.springdata.models.lessons.LessonModel.LESSON_STATE.CANCELLED
              AND l.lesson.lessonStartTimestamptz < :end
              AND l.lesson.lessonEndTimestamptz > :start
              AND (:excludeLessonId IS NULL OR l.lesson.id <> :excludeLessonId)
            """)
    List<LessonModel> findActiveOverlappingLessonsByEducator(
            @Param("tenantId") String tenantId,
            @Param("educatorId") UUID educatorId,
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("excludeLessonId") UUID excludeLessonId);
}
