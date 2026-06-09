package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import org.jspecify.annotations.Nullable;

public interface CreateLessonTopicProficiencyUseCase {
    LessonTopicProficiency create(String studentLessonReportId, String topicId, @Nullable Integer rating, String tenantId);
}
