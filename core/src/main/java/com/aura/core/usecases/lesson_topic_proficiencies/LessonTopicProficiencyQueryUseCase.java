package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonTopicProficiencyQueryUseCase {
    @Nullable LessonTopicProficiency findById(UUID id);
}
