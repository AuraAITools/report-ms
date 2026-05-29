package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonPlanModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonPlanRepository extends JpaRepository<LessonPlanModel, UUID>, JpaSpecificationExecutor<LessonPlanModel> {

    List<LessonPlanModel> findAllByLessonId(UUID lessonId);

    List<LessonPlanModel> findAllByLessonIdIn(List<UUID> lessonIds);
}
