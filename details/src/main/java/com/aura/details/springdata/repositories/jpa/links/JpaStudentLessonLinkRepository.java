package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentLessonLinkRepository extends JpaRepository<StudentLessonLinkModel, UUID>, JpaSpecificationExecutor<StudentLessonLinkModel> {

    @Query("SELECT l.lesson.id, l.student FROM StudentLessonLinkModel l WHERE l.lesson.id IN :lessonIds")
    List<Object[]> findStudentsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT l.lessonGroup.id, l.student FROM StudentLessonLinkModel l WHERE l.lessonGroup.id IN :lessonGroupIds")
    List<Object[]> findStudentsByLessonGroupIds(@Param("lessonGroupIds") List<UUID> lessonGroupIds);

    @Query("SELECT l FROM StudentLessonLinkModel l WHERE l.lesson.id = :lessonId AND l.student.id IN :studentIds")
    List<StudentLessonLinkModel> findAllByLessonIdAndStudentIdIn(@Param("lessonId") UUID lessonId,
                                                                 @Param("studentIds") List<UUID> studentIds);

    @Query("SELECT l.lesson FROM StudentLessonLinkModel l WHERE l.student.id = :studentId")
    List<LessonModel> findLessonsByStudentId(@Param("studentId") UUID studentId);

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllByStudentId(UUID studentId);

    @Query("""
            SELECT l.lesson FROM StudentLessonLinkModel l
            WHERE l.tenantId = :tenantId
              AND l.student.id = :studentId
              AND l.state = com.aura.details.springdata.models.links.StudentLessonLinkModel.STATE.REGISTERED
              AND l.lesson.state <> com.aura.details.springdata.models.lessons.LessonModel.LESSON_STATE.CANCELLED
              AND l.lesson.lessonStartTimestamptz < :end
              AND l.lesson.lessonEndTimestamptz > :start
            """)
    List<LessonModel> findActiveOverlappingLessonsByStudent(
            @Param("tenantId") String tenantId,
            @Param("studentId") UUID studentId,
            @Param("start") Instant start,
            @Param("end") Instant end);

    @Query("""
            SELECT l.lesson FROM StudentLessonLinkModel l
            WHERE l.tenantId = :tenantId
              AND l.student.id = :studentId
              AND l.state = com.aura.details.springdata.models.links.StudentLessonLinkModel.STATE.REGISTERED
              AND l.lesson.state <> com.aura.details.springdata.models.lessons.LessonModel.LESSON_STATE.CANCELLED
              AND l.lesson.lessonStartTimestamptz < :end
              AND l.lesson.lessonEndTimestamptz > :start
              AND l.lesson.id <> :excludeLessonId
            """)
    List<LessonModel> findActiveOverlappingLessonsByStudent(
            @Param("tenantId") String tenantId,
            @Param("studentId") UUID studentId,
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("excludeLessonId") UUID excludeLessonId);
}
