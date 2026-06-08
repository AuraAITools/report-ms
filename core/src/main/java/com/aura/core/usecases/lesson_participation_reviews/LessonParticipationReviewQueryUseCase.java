package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonParticipationReviewQueryUseCase {
    @Nullable LessonParticipationReview findById(UUID id);
}
