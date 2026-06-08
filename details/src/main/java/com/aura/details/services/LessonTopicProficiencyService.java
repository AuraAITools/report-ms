package com.aura.details.services;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.usecases.lesson_topic_proficiencies.DeleteLessonTopicProficiencyUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.LessonTopicProficiencyQueryUseCase;
import com.aura.core.usecases.lesson_topic_proficiencies.UpdateLessonTopicProficiencyUseCase;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonTopicProficiencyService {

    private final LessonTopicProficiencyQueryUseCase queryUseCase;
    private final UpdateLessonTopicProficiencyUseCase updateUseCase;
    private final DeleteLessonTopicProficiencyUseCase deleteUseCase;

    public @Nullable LessonTopicProficiency findById(UUID id) {
        return queryUseCase.findById(id);
    }

    @Transactional
    public @Nullable LessonTopicProficiency update(String id, Integer rating) {
        return updateUseCase.update(id, rating);
    }

    @Transactional
    public void deleteById(UUID id) {
        deleteUseCase.deleteById(id);
    }
}
