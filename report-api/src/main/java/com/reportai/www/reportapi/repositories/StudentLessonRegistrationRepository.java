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
