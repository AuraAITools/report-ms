package com.aura.core.repositories;

import com.aura.core.entities.lessons.Lesson;

public interface LessonRepository {
    Lesson save(Lesson lesson);
}
