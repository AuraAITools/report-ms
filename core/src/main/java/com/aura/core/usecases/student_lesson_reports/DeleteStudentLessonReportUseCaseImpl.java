package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.repositories.StudentLessonReportRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteStudentLessonReportUseCaseImpl implements DeleteStudentLessonReportUseCase {
    private final StudentLessonReportRepository studentLessonReportRepository;

    @Override
    public void deleteById(UUID id) {
        studentLessonReportRepository.deleteById(id);
    }
}
