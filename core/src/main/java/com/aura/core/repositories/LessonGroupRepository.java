package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonGroup;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonGroupRepository {
    LessonGroup save(LessonGroup lessonGroup);

    @Nullable LessonGroup findById(UUID id);

    List<LessonGroup> findAllByLessonId(String lessonId);

    List<LessonGroup> findAllByIds(List<UUID> ids);

    Map<String, List<LessonGroup>> findAllByLessonIds(List<String> lessonIds);

    void deleteById(UUID id);

    void deleteAllLessonGroupChildrenByLessonGroupId(String lessonGroupId);
}
