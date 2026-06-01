package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonObjectiveModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonObjectiveRepository extends JpaRepository<LessonObjectiveModel, UUID>, JpaSpecificationExecutor<LessonObjectiveModel> {
    List<LessonObjectiveModel> findAllByLessonGroupPlanId(UUID lessonGroupPlanId);

    List<LessonObjectiveModel> findAllByTenantId(String tenantId);

    @Query("SELECT lo.lessonGroupPlan.lessonGroup.lesson.id, lo FROM LessonObjectiveModel lo WHERE lo.lessonGroupPlan.lessonGroup.lesson.id IN :lessonIds")
    List<Object[]> findObjectivesByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT lo.lessonGroupPlan.id, lo FROM LessonObjectiveModel lo WHERE lo.lessonGroupPlan.id IN :planIds")
    List<Object[]> findObjectivesByLessonGroupPlanIds(@Param("planIds") List<UUID> planIds);
}
