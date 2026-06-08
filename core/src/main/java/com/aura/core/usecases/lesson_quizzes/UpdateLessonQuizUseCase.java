package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import org.jspecify.annotations.Nullable;

public interface UpdateLessonQuizUseCase {
    @Nullable LessonQuiz updateLessonQuiz(LessonQuiz lessonQuiz);
}
