package com.reportai.www.reportapi.repositories.analytics;

import com.reportai.www.reportapi.entities.lessons.LessonHomeworkCompletion;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonHomeworkCompletionRepository extends JpaRepository<LessonHomeworkCompletion, UUID>, JpaSpecificationExecutor<LessonHomeworkCompletion> {
}
