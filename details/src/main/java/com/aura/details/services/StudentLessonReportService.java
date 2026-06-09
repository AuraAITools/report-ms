package com.aura.details.services;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.DeleteStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.StudentLessonReportQueryUseCase;
import com.aura.core.usecases.student_lesson_reports.UpdateStudentLessonReportUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentLessonReportService {

    private final CreateStudentLessonReportUseCase createStudentLessonReportUseCase;
    private final StudentLessonReportQueryUseCase studentLessonReportQueryUseCase;
    private final UpdateStudentLessonReportUseCase updateStudentLessonReportUseCase;
    private final DeleteStudentLessonReportUseCase deleteStudentLessonReportUseCase;
    private final StudentLessonLinkRepository studentLessonLinkRepository;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;
    private final LessonGroupPlanTopicLinkRepository lessonGroupPlanTopicLinkRepository;

    @Transactional
    public void createReportsForStudentsInGroup(String lessonId, List<String> studentIds,
                                                List<String> topicIds, String tenantId) {
        if (studentIds.isEmpty()) return;
        List<StudentLessonLink> links = studentLessonLinkRepository
                .findAllByLessonIdAndStudentIds(lessonId, studentIds);
        for (StudentLessonLink link : links) {
            createStudentLessonReportUseCase.createEmptyReport(link.getId(), topicIds, tenantId);
        }
    }

    /**
     * Creates an empty StudentLessonReport for a given StudentLessonLink. Topic
     * proficiencies are pre-seeded from the topic list on the link's lesson-group plan,
     * so the report mirrors the structure created by the bulk auto-create path at lesson
     * creation time.
     */
    @Transactional
    public StudentLessonReport createForStudentLessonLink(String studentLessonLinkId, String tenantId) {
        List<String> topicIds = topicIdsForStudentLessonLink(studentLessonLinkId);
        return createStudentLessonReportUseCase.createEmptyReport(studentLessonLinkId, topicIds, tenantId);
    }

    private List<String> topicIdsForStudentLessonLink(String studentLessonLinkId) {
        StudentLessonLink link = studentLessonLinkRepository.findById(UUID.fromString(studentLessonLinkId));
        if (link == null) return List.of();
        LessonGroup group = link.getLessonGroup();
        if (group == null) return List.of();
        LessonGroupPlan plan = lessonGroupPlanRepository.findByLessonGroupId(group.getId());
        if (plan == null) return List.of();
        Set<String> topicIds = lessonGroupPlanTopicLinkRepository.findTopicIdsByLessonGroupPlanId(plan.getId());
        return List.copyOf(topicIds);
    }

    public @Nullable StudentLessonReport findById(UUID id) {
        return studentLessonReportQueryUseCase.findById(id);
    }

    public List<StudentLessonReport> findAllByLessonGroupId(String lessonGroupId) {
        return studentLessonReportQueryUseCase.findAllByLessonGroupId(lessonGroupId);
    }

    @Transactional
    public @Nullable StudentLessonReport update(String id,
                                                @Nullable Integer homeworkCompletion,
                                                LessonParticipationReview.@Nullable PARTICIPATION_TYPE participationType,
                                                @Nullable List<UpdateStudentLessonReportUseCase.TopicProficiencyUpdate> topicProficiencyUpdates) {
        return updateStudentLessonReportUseCase.update(id, homeworkCompletion, participationType, topicProficiencyUpdates);
    }

    @Transactional
    public void deleteById(UUID id) {
        deleteStudentLessonReportUseCase.deleteById(id);
    }
}
