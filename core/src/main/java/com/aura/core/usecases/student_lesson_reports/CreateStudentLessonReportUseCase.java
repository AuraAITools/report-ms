package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.links.StudentLessonReport;
import java.util.List;

public interface CreateStudentLessonReportUseCase {
    StudentLessonReport createEmptyReport(String studentLessonLinkId, List<String> topicIds, String tenantId);
}
