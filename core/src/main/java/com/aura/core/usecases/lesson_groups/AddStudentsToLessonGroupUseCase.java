package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.links.StudentLessonLink;
import java.util.List;

public interface AddStudentsToLessonGroupUseCase {
    List<StudentLessonLink> addStudentsToLessonGroup(String lessonGroupId, List<String> studentIds, String tenantId);
}
