package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonPlanTopicLinkRepository extends JpaRepository<LessonPlanTopicLinkModel, UUID>, JpaSpecificationExecutor<LessonPlanTopicLinkModel> {

    @Query("SELECT l.lessonPlan.id, l.topic FROM LessonPlanTopicLinkModel l WHERE l.lessonPlan.id IN :planIds")
    List<Object[]> findTopicsByLessonPlanIds(@Param("planIds") List<UUID> planIds);
}
