package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import java.util.List;

public interface LinkLessonsToCourseUseCase {
    List<Lesson> linkLessonsToCourseUseCase(List<String> lessonIds, String courseId, String tenantId);
}
