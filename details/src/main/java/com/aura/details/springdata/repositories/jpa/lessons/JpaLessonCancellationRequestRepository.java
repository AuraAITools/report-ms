package com.aura.details.springdata.repositories.jpa.lessons;

import com.aura.details.springdata.models.lessons.LessonCancellationRequestModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLessonCancellationRequestRepository extends JpaRepository<LessonCancellationRequestModel, UUID>, JpaSpecificationExecutor<LessonCancellationRequestModel> {
}

