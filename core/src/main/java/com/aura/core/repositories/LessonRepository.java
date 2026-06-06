package com.aura.core.repositories;

import com.aura.core.entities.lessons.Lesson;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonRepository {
    Lesson save(Lesson lesson);

    Lesson saveAndFlush(Lesson lesson);

    List<Lesson> updateLessons(List<Lesson> lessons);

    @Nullable Lesson findById(UUID id);

    List<Lesson> findAllById(List<UUID> ids);

    Map<String, List<Lesson>> findAllByCourseIds(List<String> courseIds);

    List<Lesson> findAllByCourseId(String courseId);

    List<Lesson> findAllByOutletId(String outletId);

    List<Lesson> findAllByTenantId(String tenantId);

    List<Lesson> findAllByEducatorId(String educatorId);

    List<Lesson> findAllByStudentId(String studentId);

    void deleteById(String id);

    void deleteAllLessonLinksByLessonId(String lessonId);
}
