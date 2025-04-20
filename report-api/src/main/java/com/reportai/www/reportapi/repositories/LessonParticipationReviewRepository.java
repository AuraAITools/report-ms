package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.lessons.LessonParticipationReview;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonParticipationReviewRepository extends JpaRepository<LessonParticipationReview, UUID> {
}