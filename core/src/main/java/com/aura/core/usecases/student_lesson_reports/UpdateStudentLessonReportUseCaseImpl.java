package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import com.aura.core.repositories.LessonParticipationReviewRepository;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import com.aura.core.repositories.StudentLessonReportRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateStudentLessonReportUseCaseImpl implements UpdateStudentLessonReportUseCase {

    private final StudentLessonReportRepository studentLessonReportRepository;
    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;
    private final LessonParticipationReviewRepository lessonParticipationReviewRepository;
    private final LessonTopicProficiencyRepository lessonTopicProficiencyRepository;

    @Override
    public @Nullable StudentLessonReport update(String id,
                                                @Nullable Integer homeworkCompletion,
                                                LessonParticipationReview.@Nullable PARTICIPATION_TYPE participationType,
                                                @Nullable List<TopicProficiencyUpdate> topicProficiencyUpdates) {
        StudentLessonReport existing = studentLessonReportRepository.findById(UUID.fromString(id));
        if (existing == null) {
            return null;
        }

        if (homeworkCompletion != null && existing.getLessonHomeworkCompletion() != null) {
            LessonHomeworkCompletion hwc = existing.getLessonHomeworkCompletion();
            hwc.setCompletion(homeworkCompletion);
            lessonHomeworkCompletionRepository.save(hwc);
        }

        if (participationType != null && existing.getLessonParticipationReview() != null) {
            LessonParticipationReview review = existing.getLessonParticipationReview();
            review.setParticipationType(participationType);
            lessonParticipationReviewRepository.save(review);
        }

        if (topicProficiencyUpdates != null && !topicProficiencyUpdates.isEmpty()) {
            Map<String, LessonTopicProficiency> byId = existing.getLessonTopicProficiencies().stream()
                    .collect(Collectors.toMap(LessonTopicProficiency::getId, Function.identity()));
            for (TopicProficiencyUpdate u : topicProficiencyUpdates) {
                LessonTopicProficiency proficiency = byId.get(u.id());
                if (proficiency != null) {
                    proficiency.setRating(u.rating());
                    lessonTopicProficiencyRepository.save(proficiency);
                }
            }
        }

        return studentLessonReportRepository.findById(UUID.fromString(id));
    }
}
