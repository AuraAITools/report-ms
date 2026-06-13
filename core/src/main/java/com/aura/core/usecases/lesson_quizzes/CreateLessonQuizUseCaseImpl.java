package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonQuizRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateLessonQuizUseCaseImpl implements CreateLessonQuizUseCase {

    private final LessonQuizRepository lessonQuizRepository;
    private final LessonGroupPlanRepository lessonGroupPlanRepository;

    @Override
    public LessonQuiz createLessonQuiz(LessonQuiz lessonQuiz) {
        LessonGroupPlan plan = ofNullable(lessonGroupPlanRepository.findById(UUID.fromString(lessonQuiz.getLessonGroupPlanId())))
                .orElseThrow(() -> new ResourceNotFoundException("Lesson group plan", lessonQuiz.getLessonGroupPlanId()));
        lessonQuiz.setTenantId(plan.getTenantId());
        return lessonQuizRepository.save(lessonQuiz);
    }
}
