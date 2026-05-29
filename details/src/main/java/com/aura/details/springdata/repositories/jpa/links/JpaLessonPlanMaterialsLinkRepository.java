package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LessonPlanMaterialsLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonPlanMaterialsLinkRepository extends JpaRepository<LessonPlanMaterialsLinkModel, UUID>, JpaSpecificationExecutor<LessonPlanMaterialsLinkModel> {

    void deleteAllByMaterialId(UUID materialId);

    @Query("SELECT lpm.lessonPlan.lesson.id, lpm.material FROM LessonPlanMaterialsLinkModel lpm WHERE lpm.lessonPlan.lesson.id IN :lessonIds")
    List<Object[]> findMaterialsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);
}
