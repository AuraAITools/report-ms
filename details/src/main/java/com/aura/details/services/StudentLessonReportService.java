package com.aura.details.services;

import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.usecases.student_lesson_reports.CreateStudentLessonReportUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentLessonReportService {

    private final CreateStudentLessonReportUseCase createStudentLessonReportUseCase;
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
}
