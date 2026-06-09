package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LevelLessonLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLevelLessonLinkRepository extends JpaRepository<LevelLessonLinkModel, UUID>, JpaSpecificationExecutor<LevelLessonLinkModel> {

    @Query("SELECT l.lesson.id, l.level FROM LevelLessonLinkModel l WHERE l.lesson.id IN :lessonIds")
    List<Object[]> findLevelsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT l.lesson.id FROM LevelLessonLinkModel l WHERE l.level.id = :levelId")
    List<UUID> findLessonIdsByLevelId(@Param("levelId") UUID levelId);

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllByLevelId(UUID levelId);
}
