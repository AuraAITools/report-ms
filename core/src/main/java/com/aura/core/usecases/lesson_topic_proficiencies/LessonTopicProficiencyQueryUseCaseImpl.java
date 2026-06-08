package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonTopicProficiencyQueryUseCaseImpl implements LessonTopicProficiencyQueryUseCase {
    private final LessonTopicProficiencyRepository lessonTopicProficiencyRepository;

    @Override
    public @Nullable LessonTopicProficiency findById(UUID id) {
        return lessonTopicProficiencyRepository.findById(id);
    }
}
