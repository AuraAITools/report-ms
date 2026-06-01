package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Create lesson plan use case")
class CreateLessonPlanUseCaseImplTest {

    @DisplayName("Should set lessonId and tenantId on plan, save it, and return the saved plan")
    @Test
    void setsLessonIdAndTenantSavesAndReturns() {
        UUID lessonUuid = UUID.randomUUID();
        Lesson lesson = Lesson.builder().id(lessonUuid.toString()).tenantId("tenant-1").name("Test Lesson").build();
        LessonPlan inputPlan = LessonPlan.builder().plan("My plan content").build();
        LessonPlan savedPlan = LessonPlan.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonUuid.toString())
                .tenantId("tenant-1")
                .plan("My plan content")
                .build();

        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubLessonPlanRepo = new StubLessonPlanRepository(savedPlan);
        var useCase = new CreateLessonPlanUseCaseImpl(stubLessonPlanRepo, stubLessonRepo);

        LessonPlan result = useCase.createLessonPlan(inputPlan, lessonUuid.toString());

        assertSame(savedPlan, result);
        assertEquals(lessonUuid.toString(), stubLessonPlanRepo.capturedPlan.getLessonId());
        assertEquals("tenant-1", stubLessonPlanRepo.capturedPlan.getTenantId());
    }

    @DisplayName("Should throw RuntimeException when lesson is not found")
    @Test
    void throwsWhenLessonNotFound() {
        UUID lessonUuid = UUID.randomUUID();
        LessonPlan inputPlan = LessonPlan.builder().plan("My plan content").build();

        var stubLessonRepo = new StubLessonRepository(null);
        var stubLessonPlanRepo = new StubLessonPlanRepository(null);
        var useCase = new CreateLessonPlanUseCaseImpl(stubLessonPlanRepo, stubLessonRepo);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.createLessonPlan(inputPlan, lessonUuid.toString()));
        assertEquals("lesson not found", ex.getMessage());
    }

    // --- Stubs ---

    private static class StubLessonPlanRepository implements LessonPlanRepository {
        private final LessonPlan saveResult;
        LessonPlan capturedPlan;

        StubLessonPlanRepository(LessonPlan saveResult) {
            this.saveResult = saveResult;
        }

        @Override
        public LessonPlan save(LessonPlan lessonPlan) {
            this.capturedPlan = lessonPlan;
            return saveResult;
        }

        @Override
        public LessonPlan findById(UUID id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonPlan> findAllByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonPlan> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<LessonPlan>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubLessonRepository implements LessonRepository {
        private final Lesson lessonToReturn;

        StubLessonRepository(Lesson lessonToReturn) {
            this.lessonToReturn = lessonToReturn;
        }

        @Override
        public Lesson save(Lesson lesson) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Lesson saveAndFlush(Lesson lesson) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> updateLessons(List<Lesson> lessons) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Lesson findById(UUID id) {
            return lessonToReturn;
        }

        @Override
        public List<Lesson> findAllById(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Lesson>> findAllByCourseIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findAllByCourseId(String courseId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findAllByOutletId(String outletId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findAllByEducatorId(String educatorId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findAllByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonLinksByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }
    }
}
