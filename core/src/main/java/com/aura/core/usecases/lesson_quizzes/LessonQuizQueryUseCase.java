package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface LessonQuizQueryUseCase {
    @Nullable LessonQuiz findById(String id);

    List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId);
}
