package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;
import org.jspecify.annotations.Nullable;

public interface UpdateLessonParticipationReviewUseCase {
    @Nullable LessonParticipationReview update(String id, LessonParticipationReview.PARTICIPATION_TYPE participationType);
}
