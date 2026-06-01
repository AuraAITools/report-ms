package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonGroupModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonGroupRepository extends JpaRepository<LessonGroupModel, UUID>, JpaSpecificationExecutor<LessonGroupModel> {

    List<LessonGroupModel> findAllByLessonId(UUID lessonId);

    List<LessonGroupModel> findAllByLessonIdIn(List<UUID> lessonIds);
}
