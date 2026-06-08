package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonQuizRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("UpdateLessonQuizUseCaseImpl")
class UpdateLessonQuizUseCaseImplTest {

    @DisplayName("Should return null when lesson quiz does not exist")
    @Test
    void returnsNullWhenNotFound() {
        StubRepo repo = new StubRepo(null, null);
        var useCase = new UpdateLessonQuizUseCaseImpl(repo);

        LessonQuiz input = LessonQuiz.builder().id("quiz-1").score(70).maxScore(100).build();
        assertNull(useCase.updateLessonQuiz(input));
    }

    @DisplayName("Should overwrite score and maxScore on the existing quiz and save it")
    @Test
    void overwritesScoreAndMaxScore() {
        LessonQuiz existing = LessonQuiz.builder()
                .id("quiz-1")
                .tenantId("tenant-1")
                .lessonGroupPlanId("plan-1")
                .score(50)
                .maxScore(100)
                .build();
        StubRepo repo = new StubRepo(existing, existing);
        var useCase = new UpdateLessonQuizUseCaseImpl(repo);

        LessonQuiz input = LessonQuiz.builder().id("quiz-1").score(90).maxScore(120).build();
        useCase.updateLessonQuiz(input);

        assertEquals(90, repo.captured.getScore());
        assertEquals(120, repo.captured.getMaxScore());
    }

    private static class StubRepo implements LessonQuizRepository {
        private final LessonQuiz findByIdResult;
        private final LessonQuiz saveResult;
        LessonQuiz captured;

        StubRepo(LessonQuiz findByIdResult, LessonQuiz saveResult) {
            this.findByIdResult = findByIdResult;
            this.saveResult = saveResult;
        }

        @Override
        public LessonQuiz save(LessonQuiz lessonQuiz) {
            this.captured = lessonQuiz;
            return saveResult;
        }

        @Override
        public LessonQuiz findById(String id) {
            return findByIdResult;
        }

        @Override
        public List<LessonQuiz> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<LessonQuiz>> findAllByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
