package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;

public interface CreateLessonUseCase {
    Lesson createLessonInCourse(Lesson lesson, String courseId);
}
