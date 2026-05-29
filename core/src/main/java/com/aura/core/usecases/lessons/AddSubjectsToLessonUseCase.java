package com.aura.core.usecases.lessons;

import com.aura.core.entities.links.SubjectLessonLink;
import java.util.List;

public interface AddSubjectsToLessonUseCase {
    List<SubjectLessonLink> addSubjectsToLesson(String lessonId, List<String> subjectIds, String tenantId);
}
