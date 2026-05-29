package com.aura.core.usecases.lessons;

import com.aura.core.entities.links.StudentLessonLink;
import java.util.List;

public interface AddStudentsToLessonUseCase {
    List<StudentLessonLink> addStudentsToLesson(String lessonId, List<String> studentIds, String tenantId);
}
