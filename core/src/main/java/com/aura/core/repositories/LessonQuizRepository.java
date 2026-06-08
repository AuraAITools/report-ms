package com.aura.core.repositories;

import com.aura.core.entities.lessons.LessonQuiz;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface LessonQuizRepository {
    LessonQuiz save(LessonQuiz lessonQuiz);

    @Nullable LessonQuiz findById(String id);

    List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId);

    Map<String, List<LessonQuiz>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds);

    void deleteById(String id);
}
