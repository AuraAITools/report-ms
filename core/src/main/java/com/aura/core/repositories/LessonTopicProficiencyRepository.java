package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonTopicProficiencyRepository {
    LessonTopicProficiency save(LessonTopicProficiency lessonTopicProficiency);

    @Nullable LessonTopicProficiency findById(UUID id);

    void deleteById(UUID id);
}
