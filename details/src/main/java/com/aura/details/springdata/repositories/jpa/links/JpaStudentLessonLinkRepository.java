package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
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

    @Query("SELECT l FROM StudentLessonLinkModel l WHERE l.lesson.id = :lessonId AND l.student.id IN :studentIds")
    List<StudentLessonLinkModel> findAllByLessonIdAndStudentIdIn(@Param("lessonId") UUID lessonId,
                                                                 @Param("studentIds") List<UUID> studentIds);

    @Query("SELECT l.lesson FROM StudentLessonLinkModel l WHERE l.student.id = :studentId")
    List<LessonModel> findLessonsByStudentId(@Param("studentId") UUID studentId);

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllByStudentId(UUID studentId);
}
