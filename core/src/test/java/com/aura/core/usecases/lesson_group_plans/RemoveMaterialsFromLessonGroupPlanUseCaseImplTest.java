package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RemoveMaterialsFromLessonGroupPlanUseCaseImpl")
class RemoveMaterialsFromLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should delegate to repository with the plan id and material ids")
    @Test
    void delegatesToRepository() {
        StubRepo repo = new StubRepo();
        var useCase = new RemoveMaterialsFromLessonGroupPlanUseCaseImpl(repo);

        useCase.removeMaterialsFromLessonGroupPlan("plan-1", List.of("m1", "m2"));

        assertEquals("plan-1", repo.capturedPlanId);
        assertEquals(List.of("m1", "m2"), repo.capturedMaterialIds);
    }

    @DisplayName("Should be a no-op when material ids is empty")
    @Test
    void noOpWhenEmpty() {
        StubRepo repo = new StubRepo();
        var useCase = new RemoveMaterialsFromLessonGroupPlanUseCaseImpl(repo);

        useCase.removeMaterialsFromLessonGroupPlan("plan-1", List.of());

        assertNull(repo.capturedPlanId);
        assertTrue(repo.capturedMaterialIds.isEmpty());
    }

    private static class StubRepo implements LessonGroupPlanMaterialLinkRepository {
        String capturedPlanId;
        List<String> capturedMaterialIds = new ArrayList<>();

        @Override
        public List<LessonGroupPlanMaterialLink> saveAll(List<LessonGroupPlanMaterialLink> links) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonGroupPlanMaterialLink> findAllByLessonGroupPlanId(String lessonGroupPlanId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllByLessonGroupPlanIdAndMaterialIdIn(String lessonGroupPlanId, List<String> materialIds) {
            this.capturedPlanId = lessonGroupPlanId;
            this.capturedMaterialIds = new ArrayList<>(materialIds);
        }
    }
}
