package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.repositories.LessonParticipationReviewRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonParticipationReviewUseCaseImpl implements DeleteLessonParticipationReviewUseCase {
    private final LessonParticipationReviewRepository lessonParticipationReviewRepository;

    @Override
    public void deleteById(UUID id) {
        lessonParticipationReviewRepository.deleteById(id);
    }
}
