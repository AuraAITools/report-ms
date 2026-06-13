package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonGroupPlanModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonGroupPlanRepository extends JpaRepository<LessonGroupPlanModel, UUID>, JpaSpecificationExecutor<LessonGroupPlanModel> {

    Optional<LessonGroupPlanModel> findByLessonGroupId(UUID lessonGroupId);

    boolean existsByLessonGroupId(UUID lessonGroupId);

    List<LessonGroupPlanModel> findAllByLessonGroupIdIn(List<UUID> lessonGroupIds);
}
