package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
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
}
