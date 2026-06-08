package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonQuizRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DeleteLessonQuizUseCaseImpl")
class DeleteLessonQuizUseCaseImplTest {

    @DisplayName("Should delegate to repository deleteById")
    @Test
    void delegatesToRepository() {
        StubRepo repo = new StubRepo();
        var useCase = new DeleteLessonQuizUseCaseImpl(repo);

        useCase.deleteById("quiz-1");

        assertEquals("quiz-1", repo.deletedId);
    }

    private static class StubRepo implements LessonQuizRepository {
        String deletedId;

        @Override
        public LessonQuiz save(LessonQuiz lessonQuiz) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonQuiz findById(String id) {
            throw new UnsupportedOperationException();
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
            this.deletedId = id;
        }
    }
}
