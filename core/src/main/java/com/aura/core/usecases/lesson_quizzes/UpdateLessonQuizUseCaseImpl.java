package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonQuizRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateLessonQuizUseCaseImpl implements UpdateLessonQuizUseCase {

    private final LessonQuizRepository lessonQuizRepository;

    @Override
    public LessonQuiz updateLessonQuiz(LessonQuiz lessonQuiz) {
        LessonQuiz existing = lessonQuizRepository.findById(lessonQuiz.getId());
        if (existing == null) {
            throw new ResourceNotFoundException("Lesson quiz", lessonQuiz.getId());
        }
        if (lessonQuiz.getName() != null) {
            existing.setName(lessonQuiz.getName());
        }
        existing.setScore(lessonQuiz.getScore());
        existing.setMaxScore(lessonQuiz.getMaxScore());
        return lessonQuizRepository.save(existing);
    }
}
