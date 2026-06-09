package com.aura.core.repositories;

import com.aura.core.entities.links.StudentLessonLink;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface StudentLessonLinkRepository {
    StudentLessonLink save(StudentLessonLink studentLessonLink);

    List<StudentLessonLink> saveAll(List<StudentLessonLink> studentLessonLinks);

    @Nullable StudentLessonLink findById(UUID id);

    List<StudentLessonLink> findAllByLessonIdAndStudentIds(String lessonId, List<String> studentIds);

    List<StudentLessonLink> assignLessonGroupToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonGroupId);
}
