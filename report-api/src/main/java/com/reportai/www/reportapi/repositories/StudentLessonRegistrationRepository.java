package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.attachments.StudentLessonRegistration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentLessonRegistrationRepository extends JpaRepository<StudentLessonRegistration, UUID>, JpaSpecificationExecutor<StudentLessonRegistration> {
    List<StudentLessonRegistration> findAllByStudent_IdInAndLesson_Id(List<UUID> studentIds, UUID lessonId);

    // Find all registrations for a single student in a single lesson
    List<StudentLessonRegistration> findAllByStudent_IdAndLesson_Id(UUID studentId, UUID lessonId);

    // This one is correct, but for consistency:
    List<StudentLessonRegistration> findAllByLesson_Id(UUID lessonId);

    /**
     * Only registers new students from the incoming studentId list
     *
     * @param studentId incoming students
     * @param lessonId
     * @param tenantId
     * @return
     */
    @Modifying
    @Query(value = "INSERT INTO student_lesson_registrations (id, student_id, lesson_id, tenant_id, created_at, updated_at) " +
            "SELECT gen_random_uuid(), s.id, :lessonId, :tenantId, NOW(), NOW() " +
            "FROM students s " +
            "WHERE s.id IN :studentIds " +
            "AND NOT EXISTS (SELECT 1 FROM student_lesson_registrations slr " +
            "                WHERE slr.student_id = s.id AND slr.lesson_id = :lessonId)", nativeQuery = true)
    int registerNewStudentLessonRegistrations(@Param("studentIds") List<UUID> studentId,
                                              @Param("lessonId") UUID lessonId,
                                              @Param("tenantId") String tenantId);

    /**
     * Deletes all student lesson registrations for a lesson that are not in the incoming studentId list
     *
     * @param studentIds
     * @param lessonId
     * @param tenantId
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM student_lesson_registrations " +
            "WHERE lesson_id = :lessonId " +
            "AND tenant_id = :tenantId " +
            "AND student_id NOT IN :studentIds", nativeQuery = true)
    int deleteStudentLessonRegistrationsNotIn(@Param("studentIds") List<UUID> studentIds,
                                              @Param("lessonId") UUID lessonId,
                                              @Param("tenantId") String tenantId);
//
//    @Modifying
//    @Query(value = "DELETE FROM student_lesson_registrations " +
//            "WHERE lesson_id = :lessonId AND tenant_id = :tenantId",
//            nativeQuery = true)
//    int deleteAllStudentLessonRegistrationsOfLesson(@Param("lessonId") UUID lessonId,
//                                                    @Param("tenantId") String tenantId);

    void deleteAllByLesson_Id(UUID lessonId);

    /**
     * @param studentId
     * @return
     */
    @Query("SELECT slr FROM StudentLessonRegistration slr " +
            "WHERE slr.student.id = :studentId " +
            "AND slr.lesson.lessonStartTimestamptz < :endTime " +
            "AND slr.lesson.lessonEndTimestamptz > :startTime"
    )
    List<StudentLessonRegistration> findOverlappingLessonRegistrationsByStudentId(
            @Param("studentId") UUID studentId, @Param("startTime") Instant startTime, @Param("endTime") Instant endTime
    );

    @Modifying
    @Query("DELETE FROM StudentLessonRegistration slr WHERE slr.student.id IN :studentIds AND slr.lesson.id = :lessonId")
    void deleteByStudentIdsAndLessonId(@Param("studentIds") List<UUID> studentIds, @Param("lessonId") UUID lessonId);
}
