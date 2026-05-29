package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentCourseLinkRepository extends JpaRepository<StudentCourseLinkModel, UUID>, JpaSpecificationExecutor<StudentCourseLinkModel> {

    @Query("SELECT l.course.id, l.student FROM StudentCourseLinkModel l WHERE l.course.id IN :courseIds")
    List<Object[]> findStudentsByCourseIds(@Param("courseIds") List<UUID> courseIds);

    @Query("SELECT l.student.id, l.course FROM StudentCourseLinkModel l WHERE l.student.id IN :studentIds")
    List<Object[]> findCoursesByStudentIds(@Param("studentIds") List<UUID> studentIds);

    void deleteAllByCourseId(UUID courseId);

    void deleteAllByStudentId(UUID studentId);
}
