package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonParticipationReviewUseCaseImpl implements UpdateLessonParticipationReviewUseCase {
    private final LessonParticipationReviewRepository lessonParticipationReviewRepository;

    @Override
    public @Nullable LessonParticipationReview update(String id, LessonParticipationReview.PARTICIPATION_TYPE participationType) {
        LessonParticipationReview existing = lessonParticipationReviewRepository.findById(UUID.fromString(id));
        if (existing == null) {
            return null;
        }
        existing.setParticipationType(participationType);
        return lessonParticipationReviewRepository.save(existing);
    }
}
