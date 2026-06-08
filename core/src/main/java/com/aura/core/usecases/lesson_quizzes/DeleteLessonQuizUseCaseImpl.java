package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.repositories.LessonQuizRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonQuizUseCaseImpl implements DeleteLessonQuizUseCase {

    private final LessonQuizRepository lessonQuizRepository;

    @Override
    public void deleteById(String id) {
        lessonQuizRepository.deleteById(id);
    }
}
