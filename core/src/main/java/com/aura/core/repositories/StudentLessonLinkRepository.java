package com.aura.core.repositories;

import com.aura.core.entities.links.StudentLessonLink;
import java.util.List;

public interface StudentLessonLinkRepository {
    StudentLessonLink save(StudentLessonLink studentLessonLink);

    List<StudentLessonLink> saveAll(List<StudentLessonLink> studentLessonLinks);

    List<StudentLessonLink> findAllByLessonIdAndStudentIds(String lessonId, List<String> studentIds);

    List<StudentLessonLink> assignLessonPlanToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonPlanId);
}
