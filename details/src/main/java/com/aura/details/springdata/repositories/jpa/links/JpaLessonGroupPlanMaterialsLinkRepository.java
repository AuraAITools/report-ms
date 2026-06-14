package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.LessonGroupPlanMaterialsLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonGroupPlanMaterialsLinkRepository extends JpaRepository<LessonGroupPlanMaterialsLinkModel, UUID>, JpaSpecificationExecutor<LessonGroupPlanMaterialsLinkModel> {

    void deleteAllByMaterialId(UUID materialId);

    List<LessonGroupPlanMaterialsLinkModel> findAllByLessonGroupPlanId(UUID lessonGroupPlanId);

    void deleteAllByLessonGroupPlanIdAndMaterialIdIn(UUID lessonGroupPlanId, List<UUID> materialIds);

    void deleteAllByLessonGroupPlan_LessonGroup_Id(UUID lessonGroupId);

    @Query("SELECT lgpm.lessonGroupPlan.lessonGroup.lesson.id, lgpm.material FROM LessonGroupPlanMaterialsLinkModel lgpm WHERE lgpm.lessonGroupPlan.lessonGroup.lesson.id IN :lessonIds")
    List<Object[]> findMaterialsByLessonIds(@Param("lessonIds") List<UUID> lessonIds);

    @Query("SELECT lgpm.lessonGroupPlan.id, lgpm.material FROM LessonGroupPlanMaterialsLinkModel lgpm WHERE lgpm.lessonGroupPlan.id IN :planIds")
    List<Object[]> findMaterialsByLessonGroupPlanIds(@Param("planIds") List<UUID> planIds);
}
