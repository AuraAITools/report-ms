package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.exceptions.DuplicateResourceException;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CreateLessonGroupPlanUseCaseImpl")
class CreateLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should set lessonGroupId and tenantId on plan, save it, and return the saved plan")
    @Test
    void setsLessonGroupIdAndTenantSavesAndReturns() {
        UUID lessonGroupUuid = UUID.randomUUID();
        LessonGroup lessonGroup = LessonGroup.builder()
                .id(lessonGroupUuid.toString())
                .tenantId("tenant-1")
                .lessonId("lesson-uuid")
                .build();
        LessonGroupPlan inputPlan = LessonGroupPlan.builder().plan("My plan content").build();
        LessonGroupPlan savedPlan = LessonGroupPlan.builder()
                .id(UUID.randomUUID().toString())
                .lessonGroupId(lessonGroupUuid.toString())
                .tenantId("tenant-1")
                .plan("My plan content")
                .build();

        var stubLessonGroupRepo = new StubLessonGroupRepository(lessonGroup);
        var stubLessonGroupPlanRepo = new StubLessonGroupPlanRepository(savedPlan);
        var useCase = new CreateLessonGroupPlanUseCaseImpl(stubLessonGroupPlanRepo, stubLessonGroupRepo);

        LessonGroupPlan result = useCase.createLessonGroupPlan(inputPlan, lessonGroupUuid.toString());

        assertSame(savedPlan, result);
        assertEquals(lessonGroupUuid.toString(), stubLessonGroupPlanRepo.capturedPlan.getLessonGroupId());
        assertEquals("tenant-1", stubLessonGroupPlanRepo.capturedPlan.getTenantId());
    }

    @DisplayName("Should throw ResourceNotFoundException when lesson group is not found")
    @Test
    void throwsWhenLessonGroupNotFound() {
        UUID lessonGroupUuid = UUID.randomUUID();
        LessonGroupPlan inputPlan = LessonGroupPlan.builder().plan("My plan content").build();

        var stubLessonGroupRepo = new StubLessonGroupRepository(null);
        var stubLessonGroupPlanRepo = new StubLessonGroupPlanRepository(null);
        var useCase = new CreateLessonGroupPlanUseCaseImpl(stubLessonGroupPlanRepo, stubLessonGroupRepo);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> useCase.createLessonGroupPlan(inputPlan, lessonGroupUuid.toString()));
        assertEquals("Lesson group not found: " + lessonGroupUuid, ex.getMessage());
    }

    @DisplayName("Should throw DuplicateResourceException when a plan already exists for the lesson group")
    @Test
    void throwsWhenPlanAlreadyExistsForLessonGroup() {
        UUID lessonGroupUuid = UUID.randomUUID();
        LessonGroup lessonGroup = LessonGroup.builder()
                .id(lessonGroupUuid.toString())
                .tenantId("tenant-1")
                .lessonId("lesson-uuid")
                .build();
        LessonGroupPlan inputPlan = LessonGroupPlan.builder().plan("My plan content").build();

        var stubLessonGroupRepo = new StubLessonGroupRepository(lessonGroup);
        var stubLessonGroupPlanRepo = new StubLessonGroupPlanRepository(null);
        stubLessonGroupPlanRepo.planExists = true;
        var useCase = new CreateLessonGroupPlanUseCaseImpl(stubLessonGroupPlanRepo, stubLessonGroupRepo);

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> useCase.createLessonGroupPlan(inputPlan, lessonGroupUuid.toString()));
        assertEquals(
                "Lesson group plan already exists for lesson group '" + lessonGroupUuid + "'",
                ex.getMessage());
        assertNull(stubLessonGroupPlanRepo.capturedPlan);
    }

    // --- Stubs ---

    private static class StubLessonGroupPlanRepository implements LessonGroupPlanRepository {
        private final LessonGroupPlan saveResult;
        LessonGroupPlan capturedPlan;
        boolean planExists;

        StubLessonGroupPlanRepository(LessonGroupPlan saveResult) {
            this.saveResult = saveResult;
        }

        @Override
        public LessonGroupPlan save(LessonGroupPlan plan) {
            this.capturedPlan = plan;
            return saveResult;
        }

        @Override
        public boolean existsByLessonGroupId(String lessonGroupId) {
            return planExists;
        }

        @Override
        public LessonGroupPlan findById(UUID id) {
            throw new UnsupportedOperationException();
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

    private static class StubLessonGroupRepository implements LessonGroupRepository {
        private final LessonGroup lessonGroupToReturn;

        StubLessonGroupRepository(LessonGroup lessonGroupToReturn) {
            this.lessonGroupToReturn = lessonGroupToReturn;
        }

        @Override
        public LessonGroup save(LessonGroup lessonGroup) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonGroup findById(UUID id) {
            return lessonGroupToReturn;
        }

        @Override
        public List<LessonGroup> findAllByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonGroup> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<LessonGroup>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }
    }
}
