package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.TopicLessonLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTopicLessonLinkRepository extends JpaRepository<TopicLessonLinkModel, UUID>, JpaSpecificationExecutor<TopicLessonLinkModel> {

    void deleteAllByLessonId(UUID lessonId);

    void deleteAllByTopicId(UUID topicId);

    @Query("SELECT l.topic.id FROM TopicLessonLinkModel l WHERE l.lesson.id = :lessonId")
    List<UUID> findTopicIdsByLessonId(@Param("lessonId") UUID lessonId);

    @Query("SELECT l.lesson.id FROM TopicLessonLinkModel l WHERE l.topic.id = :topicId")
    List<UUID> findLessonIdsByTopicId(@Param("topicId") UUID topicId);

    @Query("SELECT l.lesson.id, l.topic FROM TopicLessonLinkModel l WHERE l.lesson.id IN :lessonIds")
    List<Object[]> findTopicsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);
}
