package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectLessonLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectLessonLinkRepository extends JpaRepository<SubjectLessonLinkModel, UUID>, JpaSpecificationExecutor<SubjectLessonLinkModel> {

    @Query("SELECT l.lesson.id, l.subject FROM SubjectLessonLinkModel l WHERE l.lesson.id IN :lessonIds")
    List<Object[]> findSubjectsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT l.lesson.id FROM SubjectLessonLinkModel l WHERE l.subject.id = :subjectId")
    List<UUID> findLessonIdsBySubjectId(@Param("subjectId") UUID subjectId);

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllBySubjectId(UUID subjectId);
}
