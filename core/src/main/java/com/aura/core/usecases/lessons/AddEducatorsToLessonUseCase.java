package com.aura.core.usecases.lessons;

import com.aura.core.entities.links.EducatorLessonLink;
import java.util.List;

public interface AddEducatorsToLessonUseCase {
    List<EducatorLessonLink> addEducatorsToLessonUseCase(String lessonId, List<String> educatorIds, String tenantId);
}
