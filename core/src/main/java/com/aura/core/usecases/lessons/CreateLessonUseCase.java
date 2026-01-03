package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;

public interface CreateLessonUseCase {
    Lesson createStandaloneLesson(Lesson lesson, String outletId);
}
