package com.aura.core.usecases.lessons;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Create lesson use case")
class CreateLessonUseCaseImplTest {

    @DisplayName("Should set outlet and course on lesson, save it, and return the saved lesson")
    @Test
    void setsOutletAndCourseOnLessonSavesAndReturns() {
        Course course = Course.builder().id("course-1").name("Test Course").outletId("outlet-1").build();
        Lesson inputLesson = Lesson.builder().id(UUID.randomUUID().toString()).name("Test Lesson").build();
        Lesson savedLesson = Lesson.builder().id(inputLesson.getId()).name("Test Lesson").outletId("outlet-1").courseId("course-1").build();

        var stubLessonRepo = new StubLessonRepository(savedLesson);
        var stubCourseRepo = new StubCourseRepository(course);
        var useCase = new CreateLessonUseCaseImpl(stubLessonRepo, stubCourseRepo);

        Lesson result = useCase.createLessonInCourse(inputLesson, "course-1");

        assertEquals(savedLesson, result);
        assertEquals("outlet-1", stubLessonRepo.capturedLesson.getOutletId());
        assertEquals("course-1", stubLessonRepo.capturedLesson.getCourseId());
    }

    @DisplayName("Should throw RuntimeException when course is not found")
    @Test
    void throwsWhenCourseNotFound() {
        Lesson inputLesson = Lesson.builder().id(UUID.randomUUID().toString()).name("Test Lesson").build();

        var stubLessonRepo = new StubLessonRepository(null);
        var stubCourseRepo = new StubCourseRepository(null);
        var useCase = new CreateLessonUseCaseImpl(stubLessonRepo, stubCourseRepo);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.createLessonInCourse(inputLesson, "course-1"));
        assertEquals("course not found", ex.getMessage());
    }

    // --- Stubs ---

    private static class StubLessonRepository implements LessonRepository {
        private final Lesson lessonToReturn;
        Lesson capturedLesson;

        StubLessonRepository(Lesson lessonToReturn) {
            this.lessonToReturn = lessonToReturn;
        }

        @Override
        public Lesson save(Lesson lesson) {
            this.capturedLesson = lesson;
            return lessonToReturn;
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
            throw new UnsupportedOperationException();
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

    private static class StubOutletRepository implements OutletRepository {
        private final Outlet outletToReturn;

        StubOutletRepository(Outlet outletToReturn) {
            this.outletToReturn = outletToReturn;
        }

        @Override
        public Outlet findById(String id) {
            return outletToReturn;
        }

        @Override
        public Outlet save(Outlet outlet) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Outlet> findAllByIds(List<String> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Outlet> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Outlet> findAllByTenantIds(Set<String> tenantIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Outlet>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubCourseRepository implements CourseRepository {
        private final Course courseToReturn;

        StubCourseRepository(Course courseToReturn) {
            this.courseToReturn = courseToReturn;
        }

        @Override
        public Course save(Course course) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Course findById(String id) {
            return courseToReturn;
        }

        @Override
        public List<Course> findAllByOutletId(String outletId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Course> findAllByIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Course>> findAllByStudentIds(List<String> studentIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Course>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void nullOutLevelForCourses(String levelId) {
            throw new UnsupportedOperationException();
        }
    }
}
