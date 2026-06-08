package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonQuizModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonQuizRepository extends JpaRepository<LessonQuizModel, UUID>, JpaSpecificationExecutor<LessonQuizModel> {

    List<LessonQuizModel> findAllByLessonGroupPlanId(UUID lessonGroupPlanId);

    @Query("SELECT q.lessonGroupPlan.id, q FROM LessonQuizModel q WHERE q.lessonGroupPlan.id IN :planIds")
    List<Object[]> findQuizzesByLessonGroupPlanIds(@Param("planIds") List<UUID> planIds);
}

