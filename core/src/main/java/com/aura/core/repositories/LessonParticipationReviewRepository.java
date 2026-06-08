package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonParticipationReview;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonParticipationReviewRepository {
    LessonParticipationReview save(LessonParticipationReview lessonParticipationReview);

    @Nullable LessonParticipationReview findById(UUID id);

    void deleteById(UUID id);
}
