package com.aura.core.usecases.lesson_quizzes;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonQuiz;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonQuizRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CreateLessonQuizUseCaseImpl")
class CreateLessonQuizUseCaseImplTest {

    @DisplayName("Should set tenantId from plan and save the quiz")
    @Test
    void setsTenantIdAndSaves() {
        UUID planUuid = UUID.randomUUID();
        LessonGroupPlan plan = LessonGroupPlan.builder()
                .id(planUuid.toString())
                .tenantId("tenant-1")
                .build();
        LessonQuiz toCreate = LessonQuiz.builder()
                .lessonGroupPlanId(planUuid.toString())
                .score(80)
                .maxScore(100)
                .build();
        LessonQuiz saved = LessonQuiz.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupPlanId(planUuid.toString())
                .tenantId("tenant-1")
                .score(80)
                .maxScore(100)
                .build();

        StubPlanRepo planRepo = new StubPlanRepo(plan);
        StubQuizRepo quizRepo = new StubQuizRepo(saved);
        var useCase = new CreateLessonQuizUseCaseImpl(quizRepo, planRepo);

        LessonQuiz result = useCase.createLessonQuiz(toCreate);

        assertSame(saved, result);
        assertEquals("tenant-1", quizRepo.captured.getTenantId());
    }

    @DisplayName("Should throw when lesson group plan is not found")
    @Test
    void throwsWhenPlanMissing() {
        UUID planUuid = UUID.randomUUID();
        LessonQuiz toCreate = LessonQuiz.builder()
                .lessonGroupPlanId(planUuid.toString())
                .build();

        StubPlanRepo planRepo = new StubPlanRepo(null);
        StubQuizRepo quizRepo = new StubQuizRepo(null);
        var useCase = new CreateLessonQuizUseCaseImpl(quizRepo, planRepo);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.createLessonQuiz(toCreate));
        assertEquals("lesson group plan not found", ex.getMessage());
    }

    private static class StubPlanRepo implements LessonGroupPlanRepository {
        private final LessonGroupPlan toReturn;

        StubPlanRepo(LessonGroupPlan toReturn) {
            this.toReturn = toReturn;
        }

        @Override
        public LessonGroupPlan save(LessonGroupPlan plan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonGroupPlan findById(UUID id) {
            return toReturn;
        }

        @Override
        public LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonGroupPlan> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, LessonGroupPlan> findAllByLessonGroupIds(List<String> lessonGroupIds) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubQuizRepo implements LessonQuizRepository {
        private final LessonQuiz saveResult;
        LessonQuiz captured;

        StubQuizRepo(LessonQuiz saveResult) {
            this.saveResult = saveResult;
        }

        @Override
        public LessonQuiz save(LessonQuiz lessonQuiz) {
            this.captured = lessonQuiz;
            return saveResult;
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
            throw new UnsupportedOperationException();
        }
    }
}
