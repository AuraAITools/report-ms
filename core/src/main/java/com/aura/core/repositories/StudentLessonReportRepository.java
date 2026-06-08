package com.aura.core.repositories;

import com.aura.core.entities.links.StudentLessonReport;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StudentLessonReportRepository {
    StudentLessonReport save(StudentLessonReport studentLessonReport);

    @Nullable StudentLessonReport findById(UUID id);

    List<StudentLessonReport> findAllByLessonGroupId(String lessonGroupId);

    void deleteById(UUID id);
}
