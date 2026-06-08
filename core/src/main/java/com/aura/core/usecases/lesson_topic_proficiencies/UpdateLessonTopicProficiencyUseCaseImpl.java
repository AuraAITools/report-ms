package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonTopicProficiencyUseCaseImpl implements UpdateLessonTopicProficiencyUseCase {
    private final LessonTopicProficiencyRepository lessonTopicProficiencyRepository;

    @Override
    public @Nullable LessonTopicProficiency update(String id, Integer rating) {
        LessonTopicProficiency existing = lessonTopicProficiencyRepository.findById(UUID.fromString(id));
        if (existing == null) {
            return null;
        }
        existing.setRating(rating);
        return lessonTopicProficiencyRepository.save(existing);
    }
}
