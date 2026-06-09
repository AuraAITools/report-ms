package com.aura.core.repositories;

import com.aura.core.entities.links.StudentLessonReport;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StudentLessonReportRepository {
    StudentLessonReport save(StudentLessonReport studentLessonReport);

    @Nullable StudentLessonReport findById(UUID id);

    @Nullable StudentLessonReport findByStudentLessonLinkId(String studentLessonLinkId);

    List<StudentLessonReport> findAllByLessonGroupId(String lessonGroupId);

    /**
     * For each given lesson group id, returns the student-id → report mapping for
     * students in that group. Used by the LessonGroupResponse.lessonStudents batch
     * resolver. A studentId is absent from the inner map when no report exists yet
     * for that student-lesson-link.
     */
    Map<String, Map<String, StudentLessonReport>> findReportsByLessonGroupIds(List<String> lessonGroupIds);

    void deleteById(UUID id);
}
