package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LessonGroupPlanTopicLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonGroupPlanTopicLinkRepository extends JpaRepository<LessonGroupPlanTopicLinkModel, UUID>, JpaSpecificationExecutor<LessonGroupPlanTopicLinkModel> {

    @Query("SELECT l.lessonGroupPlan.id, l.topic FROM LessonGroupPlanTopicLinkModel l WHERE l.lessonGroupPlan.id IN :planIds")
    List<Object[]> findTopicsByLessonGroupPlanIds(@Param("planIds") List<UUID> planIds);

    @Query("SELECT l.topic.id FROM LessonGroupPlanTopicLinkModel l WHERE l.lessonGroupPlan.id = :planId")
    List<UUID> findTopicIdsByLessonGroupPlanId(@Param("planId") UUID planId);

    void deleteAllByLessonGroupPlanIdAndTopicIdIn(UUID lessonGroupPlanId, List<UUID> topicIds);
}
