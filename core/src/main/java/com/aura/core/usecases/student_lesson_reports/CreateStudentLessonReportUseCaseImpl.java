package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.StudentLessonReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateStudentLessonReportUseCaseImpl implements CreateStudentLessonReportUseCase {
    private final StudentLessonReportRepository studentLessonReportRepository;

    @Override
    public StudentLessonReport createEmptyReport(String studentLessonLinkId, List<String> topicIds, String tenantId) {
        LessonHomeworkCompletion homeworkCompletion = LessonHomeworkCompletion.builder()
                .tenantId(tenantId)
                .build();

        LessonParticipationReview participationReview = LessonParticipationReview.builder()
                .tenantId(tenantId)
                .participationType(LessonParticipationReview.PARTICIPATION_TYPE.UNFILLED)
                .build();

        List<LessonTopicProficiency> proficiencies = topicIds.stream()
                .map(topicId -> LessonTopicProficiency.builder()
                        .tenantId(tenantId)
                        .topicId(topicId)
                        .build())
                .toList();

        StudentLessonReport report = StudentLessonReport.builder()
                .tenantId(tenantId)
                .studentLessonLinkId(studentLessonLinkId)
                .lessonHomeworkCompletion(homeworkCompletion)
                .lessonParticipationReview(participationReview)
                .lessonTopicProficiencies(proficiencies)
                .build();

        return studentLessonReportRepository.save(report);
    }
}
