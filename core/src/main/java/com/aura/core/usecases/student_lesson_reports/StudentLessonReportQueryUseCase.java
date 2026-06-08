package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.links.StudentLessonReport;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StudentLessonReportQueryUseCase {
    @Nullable StudentLessonReport findById(UUID id);

    List<StudentLessonReport> findAllByLessonGroupId(String lessonGroupId);
}
