package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.links.StudentLessonReport;
import com.aura.core.repositories.StudentLessonReportRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class StudentLessonReportQueryUseCaseImpl implements StudentLessonReportQueryUseCase {
    private final StudentLessonReportRepository studentLessonReportRepository;

    @Override
    public @Nullable StudentLessonReport findById(UUID id) {
        return studentLessonReportRepository.findById(id);
    }

    @Override
    public List<StudentLessonReport> findAllByLessonGroupId(String lessonGroupId) {
        return studentLessonReportRepository.findAllByLessonGroupId(lessonGroupId);
    }
}
