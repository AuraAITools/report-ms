package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.repositories.LessonTopicProficiencyRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonTopicProficiencyUseCaseImpl implements DeleteLessonTopicProficiencyUseCase {
    private final LessonTopicProficiencyRepository lessonTopicProficiencyRepository;

    @Override
    public void deleteById(UUID id) {
        lessonTopicProficiencyRepository.deleteById(id);
    }
}
