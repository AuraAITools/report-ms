package com.aura.core.usecases.lessons;

import com.aura.core.entities.educators.Educator;
import java.util.List;

public interface AddEducatorsToLessonUseCase {
    List<Educator> addEducatorsToLessonUseCase(String lessonId, List<String> educatorIds, String tenantId);
}
