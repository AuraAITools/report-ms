package com.aura.details.services;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.DeleteStudentLessonReportUseCase;
import com.aura.core.usecases.student_lesson_reports.StudentLessonReportQueryUseCase;
import com.aura.core.usecases.student_lesson_reports.UpdateStudentLessonReportUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
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
