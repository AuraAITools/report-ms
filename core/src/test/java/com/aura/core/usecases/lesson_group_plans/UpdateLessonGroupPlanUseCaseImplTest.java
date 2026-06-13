package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.repositories.LessonGroupPlanRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("UpdateLessonGroupPlanUseCaseImpl")
class UpdateLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should return null when lesson group plan is not found")
    @Test
    void returnsNullWhenNotFound() {
        UUID planId = UUID.randomUUID();
        StubLessonGroupPlanRepository repo = new StubLessonGroupPlanRepository(null, null);
        var useCase = new UpdateLessonGroupPlanUseCaseImpl(repo);

        LessonGroupPlan input = LessonGroupPlan.builder().id(planId.toString()).plan("new").build();
        assertNull(useCase.updateLessonGroupPlan(input));
    }

    @DisplayName("Should update only the fields that are non-null on the input")
    @Test
    void updatesOnlyProvidedFields() {
        UUID planId = UUID.randomUUID();
        LessonGroupPlan existing = LessonGroupPlan.builder()
                .id(planId.toString())
                .tenantId("tenant-1")
                .lessonGroupId("lg-1")
                .plan("original")
                .recap("original recap")
                .state(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.UNPLANNED)
                .build();
        StubLessonGroupPlanRepository repo = new StubLessonGroupPlanRepository(existing, existing);
        var useCase = new UpdateLessonGroupPlanUseCaseImpl(repo);

        LessonGroupPlan input = LessonGroupPlan.builder()
                .id(planId.toString())
                .plan("updated")
                .build();
        LessonGroupPlan result = useCase.updateLessonGroupPlan(input);

        assertSame(existing, result);
        assertEquals("updated", repo.capturedPlan.getPlan());
        assertEquals("original recap", repo.capturedPlan.getRecap());
        assertEquals(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.UNPLANNED, repo.capturedPlan.getState());
    }

    @DisplayName("Should update plan recap and state when all three are provided")
    @Test
    void updatesAllProvidedFields() {
        UUID planId = UUID.randomUUID();
        LessonGroupPlan existing = LessonGroupPlan.builder()
                .id(planId.toString())
                .tenantId("tenant-1")
                .lessonGroupId("lg-1")
                .plan("original")
                .recap(null)
                .state(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.UNPLANNED)
                .build();
        StubLessonGroupPlanRepository repo = new StubLessonGroupPlanRepository(existing, existing);
        var useCase = new UpdateLessonGroupPlanUseCaseImpl(repo);

        LessonGroupPlan input = LessonGroupPlan.builder()
                .id(planId.toString())
                .plan("new plan")
                .recap("new recap")
                .state(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.PLANNED)
                .build();
        useCase.updateLessonGroupPlan(input);

        assertEquals("new plan", repo.capturedPlan.getPlan());
        assertEquals("new recap", repo.capturedPlan.getRecap());
        assertEquals(LessonGroupPlan.LESSON_GROUP_PLAN_STATE.PLANNED, repo.capturedPlan.getState());
    }

    private static class StubLessonGroupPlanRepository implements LessonGroupPlanRepository {
        private final LessonGroupPlan findByIdResult;
        private final LessonGroupPlan saveResult;
        LessonGroupPlan capturedPlan;

        StubLessonGroupPlanRepository(LessonGroupPlan findByIdResult, LessonGroupPlan saveResult) {
            this.findByIdResult = findByIdResult;
            this.saveResult = saveResult;
        }

        @Override
        public LessonGroupPlan save(LessonGroupPlan plan) {
            this.capturedPlan = plan;
            return saveResult;
        }

        @Override
        public LessonGroupPlan findById(UUID id) {
            return findByIdResult;
        }

        @Override
        public LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean existsByLessonGroupId(String lessonGroupId) {
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
}
