package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonQuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonQuizQueryUseCaseImpl implements LessonQuizQueryUseCase {
    private final LessonQuizRepository lessonQuizRepository;

    @Override
    public @Nullable LessonQuiz findById(String id) {
        return lessonQuizRepository.findById(id);
    }

    @Override
    public List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return lessonQuizRepository.findAllByLessonGroupPlanId(lessonGroupPlanId);
    }
}
