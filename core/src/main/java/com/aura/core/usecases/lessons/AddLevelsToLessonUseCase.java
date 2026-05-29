package com.aura.core.usecases.lessons;

import com.aura.core.entities.links.LevelLessonLink;
import java.util.List;

public interface AddLevelsToLessonUseCase {
    List<LevelLessonLink> addLevelsToLesson(String lessonId, List<String> levelIds, String tenantId);
}
