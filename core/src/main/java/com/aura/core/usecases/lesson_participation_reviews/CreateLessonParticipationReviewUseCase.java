package com.aura.core.usecases.lesson_participation_reviews;

import com.aura.core.entities.lessons.LessonParticipationReview;

public interface CreateLessonParticipationReviewUseCase {
    LessonParticipationReview create(String studentLessonReportId,
                                     LessonParticipationReview.PARTICIPATION_TYPE participationType,
                                     String tenantId);
}
