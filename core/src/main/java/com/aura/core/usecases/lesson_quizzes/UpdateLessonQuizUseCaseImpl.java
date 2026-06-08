package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonQuizRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateLessonQuizUseCaseImpl implements UpdateLessonQuizUseCase {

    private final LessonQuizRepository lessonQuizRepository;

    @Override
    public @Nullable LessonQuiz updateLessonQuiz(LessonQuiz lessonQuiz) {
        LessonQuiz existing = lessonQuizRepository.findById(lessonQuiz.getId());
        if (existing == null) {
            return null;
        }
        existing.setScore(lessonQuiz.getScore());
        existing.setMaxScore(lessonQuiz.getMaxScore());
        return lessonQuizRepository.save(existing);
    }
}
