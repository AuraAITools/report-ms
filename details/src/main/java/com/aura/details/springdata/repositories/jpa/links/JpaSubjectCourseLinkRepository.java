package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectCourseLinkRepository extends JpaRepository<SubjectCourseLinkModel, UUID>, JpaSpecificationExecutor<SubjectCourseLinkModel> {

    @Query("SELECT l.course.id, l.subject FROM SubjectCourseLinkModel l WHERE l.course.id IN :courseIds")
    List<Object[]> findSubjectsByCourseIds(@Param("courseIds") List<UUID> courseIds);

    @Query("SELECT l.course.id FROM SubjectCourseLinkModel l WHERE l.subject.id = :subjectId")
    List<UUID> findCourseIdsBySubjectId(@Param("subjectId") UUID subjectId);

    void deleteAllBySubjectId(UUID subjectId);

    void deleteAllByCourseId(UUID courseId);
}
