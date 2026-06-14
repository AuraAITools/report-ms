package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CreateLessonGroupUseCaseImpl")
class CreateLessonGroupUseCaseImplTest {

    @DisplayName("Should create lesson group with lessonId and tenantId from lesson")
    @Test
    void setsLessonIdAndTenantIdFromLesson() {
        String lessonId = UUID.randomUUID().toString();
        Lesson lesson = Lesson.builder().id(lessonId).tenantId("tenant-1").name("Test Lesson").build();
        LessonGroup savedGroup = LessonGroup.builder()
                .id(UUID.randomUUID().toString())
                .lessonId(lessonId)
                .tenantId("tenant-1")
                .name("Group A")
                .build();

        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubLessonGroupRepo = new StubLessonGroupRepository(savedGroup);
        var useCase = new CreateLessonGroupUseCaseImpl(stubLessonGroupRepo, stubLessonRepo);

        LessonGroup result = useCase.createLessonGroup(LessonGroup.builder().name("Group A").build(), lessonId);

        assertEquals(lessonId, stubLessonGroupRepo.capturedGroup.getLessonId());
        assertEquals("tenant-1", stubLessonGroupRepo.capturedGroup.getTenantId());
        assertEquals(lessonId, result.getLessonId());
        assertEquals("tenant-1", result.getTenantId());
    }

    @DisplayName("Should throw ResourceNotFoundException when lesson not found")
    @Test
    void throwsWhenLessonNotFound() {
        var stubLessonRepo = new StubLessonRepository(null);
        var stubLessonGroupRepo = new StubLessonGroupRepository(null);
        var useCase = new CreateLessonGroupUseCaseImpl(stubLessonGroupRepo, stubLessonRepo);
        String lessonId = UUID.randomUUID().toString();

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> useCase.createLessonGroup(LessonGroup.builder().name("Group A").build(), lessonId));
        assertEquals("Lesson not found: " + lessonId, ex.getMessage());
    }

    // --- Stubs ---

    private static class StubLessonGroupRepository implements LessonGroupRepository {
        private final LessonGroup saveResult;
        LessonGroup capturedGroup;

        StubLessonGroupRepository(LessonGroup saveResult) {
            this.saveResult = saveResult;
        }

        @Override
        public LessonGroup save(LessonGroup lessonGroup) {
            this.capturedGroup = lessonGroup;
            return saveResult;
        }

        @Override
        public LessonGroup findById(UUID id) {
            throw new UnsupportedOperationException();
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

        @Override
        public void deleteById(UUID id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonGroupChildrenByLessonGroupId(String lessonGroupId) {
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
        public Map<String, List<Lesson>> findAllByCourseIds(List<String> courseIds) {
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
        public List<Lesson> findAllByTenantId(String tenantId) {
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

        @Override
        public List<Lesson> findActiveOverlappingForStudent(String tenantId, String studentId,
                java.time.Instant start, java.time.Instant end,
                @org.jspecify.annotations.Nullable String excludeLessonId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findActiveOverlappingForEducator(String tenantId, String educatorId,
                java.time.Instant start, java.time.Instant end,
                @org.jspecify.annotations.Nullable String excludeLessonId) {
            throw new UnsupportedOperationException();
        }
    }
}
