package com.aura.core.repositories;

import com.aura.core.entities.lessons.Lesson;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonRepository {
    Lesson save(Lesson lesson);

    List<Lesson> updateLessons(List<Lesson> lessons);

    @Nullable Lesson findById(UUID id);

    List<Lesson> findAllById(List<UUID> ids);
}
