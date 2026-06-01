package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.lessons.LessonGroup;

public interface CreateLessonGroupUseCase {
    LessonGroup createLessonGroup(LessonGroup lessonGroup, String lessonId);
}
