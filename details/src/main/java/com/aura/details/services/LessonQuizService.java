package com.aura.details.services;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.usecases.lesson_quizzes.CreateLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.DeleteLessonQuizUseCase;
import com.aura.core.usecases.lesson_quizzes.LessonQuizQueryUseCase;
import com.aura.core.usecases.lesson_quizzes.UpdateLessonQuizUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonQuizService {

    private final CreateLessonQuizUseCase createLessonQuizUseCase;
    private final UpdateLessonQuizUseCase updateLessonQuizUseCase;
    private final DeleteLessonQuizUseCase deleteLessonQuizUseCase;
    private final LessonQuizQueryUseCase lessonQuizQueryUseCase;

    @Transactional
    public LessonQuiz createLessonQuiz(LessonQuiz lessonQuiz) {
        return createLessonQuizUseCase.createLessonQuiz(lessonQuiz);
    }

    @Transactional
    public @Nullable LessonQuiz updateLessonQuiz(LessonQuiz lessonQuiz) {
        return updateLessonQuizUseCase.updateLessonQuiz(lessonQuiz);
    }

    @Transactional
    public void deleteById(String id) {
        deleteLessonQuizUseCase.deleteById(id);
    }

    public @Nullable LessonQuiz findById(String id) {
        return lessonQuizQueryUseCase.findById(id);
    }

    public List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
        return lessonQuizQueryUseCase.findAllByLessonGroupPlanId(lessonGroupPlanId);
    }
}
