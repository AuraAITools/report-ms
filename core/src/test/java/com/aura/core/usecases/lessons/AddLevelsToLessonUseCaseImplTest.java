package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.LevelLessonLink;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.LevelLessonLinkRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add levels to lesson use case")
class AddLevelsToLessonUseCaseImplTest {

    @DisplayName("Should return empty list when levelIds is empty")
    @Test
    void returnsEmptyListWhenLevelIdsEmpty() {
        var stubLinkRepo = new StubLevelLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(null);
        var stubLevelRepo = new StubLevelRepository(List.of());
        var useCase = new AddLevelsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubLevelRepo);

        List<LevelLessonLink> result = useCase.addLevelsToLesson("lesson-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create level-lesson links for each level ID")
    @Test
    void createsLevelLessonLinksForEachLevelId() {
        UUID lessonUuid = UUID.randomUUID();
        UUID levelUuid1 = UUID.randomUUID();
        UUID levelUuid2 = UUID.randomUUID();

        Lesson lesson = Lesson.builder().id(lessonUuid.toString()).name("Test Lesson").build();
        Level level1 = Level.builder().id(levelUuid1.toString()).name("Level 1").build();
        Level level2 = Level.builder().id(levelUuid2.toString()).name("Level 2").build();

        var stubLinkRepo = new StubLevelLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubLevelRepo = new StubLevelRepository(List.of(level1, level2));
        var useCase = new AddLevelsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubLevelRepo);

        List<LevelLessonLink> result = useCase.addLevelsToLesson(
                lessonUuid.toString(),
                List.of(levelUuid1.toString(), levelUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(lesson.getId(), link.getLessonId()));
        var levelIds = result.stream().map(LevelLessonLink::getLevelId).toList();
        assertTrue(levelIds.contains(levelUuid1.toString()));
        assertTrue(levelIds.contains(levelUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubLevelLessonLinkRepository implements LevelLessonLinkRepository {
        final List<List<LevelLessonLink>> savedAll = new ArrayList<>();

        @Override
        public LevelLessonLink save(LevelLessonLink levelLessonLink) {
            return levelLessonLink;
        }

        @Override
        public List<LevelLessonLink> saveAll(List<LevelLessonLink> levelLessonLinks) {
            savedAll.add(levelLessonLinks);
            return levelLessonLinks;
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
    }

    private static class StubLevelRepository implements LevelRepository {
        private final List<Level> levelsToReturn;

        StubLevelRepository(List<Level> levelsToReturn) {
            this.levelsToReturn = levelsToReturn;
        }

        @Override
        public PagedResult<Level> findAllBy(String tenantId, KeysetPaginationCriteria k, List<SortCriteria> s, SearchCriteria sc) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PagedResult<Level> findAllBy(KeysetPaginationCriteria k, List<SortCriteria> s, SearchCriteria sc) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Level findByNameAndTenantId(String name, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Level findById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Level> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Level> findAllByIds(List<UUID> levelIds) {
            return levelsToReturn;
        }

        @Override
        public java.util.Map<String, List<Level>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, Level> findAllByCourseIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Level>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLevelLessonLinksByLevelId(String levelId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Level save(Level level) {
            throw new UnsupportedOperationException();
        }
    }
}
