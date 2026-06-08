package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonParticipationReviewQueryUseCaseImpl implements LessonParticipationReviewQueryUseCase {
    private final LessonParticipationReviewRepository lessonParticipationReviewRepository;

    @Override
    public @Nullable LessonParticipationReview findById(UUID id) {
        return lessonParticipationReviewRepository.findById(id);
    }
}
