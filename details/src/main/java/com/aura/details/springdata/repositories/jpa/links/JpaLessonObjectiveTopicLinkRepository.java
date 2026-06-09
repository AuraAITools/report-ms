package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonObjectiveTopicLinkRepository extends JpaRepository<LessonObjectiveTopicLinkModel, UUID>, JpaSpecificationExecutor<LessonObjectiveTopicLinkModel> {
    void deleteAllByLessonObjectiveId(UUID lessonObjectiveId);

    void deleteAllByTopicId(UUID topicId);

    @Query("SELECT l.lessonObjective.id FROM LessonObjectiveTopicLinkModel l WHERE l.topic.id = :topicId")
    List<UUID> findLessonObjectiveIdsByTopicId(@Param("topicId") UUID topicId);
}
