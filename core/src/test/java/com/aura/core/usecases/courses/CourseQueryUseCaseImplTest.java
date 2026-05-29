package com.aura.core.usecases.courses;

import com.aura.core.entities.courses.Course;
import com.aura.core.repositories.CourseRepository;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Course query use case")
class CourseQueryUseCaseImplTest {

    @DisplayName("Should return the course found by id")
    @Test
    void returnsCourseFoundById() {
        Course course = Course.builder().id("course-1").name("Algebra").build();
        var stub = new StubCourseRepository();
        stub.findByIdResult = course;
        var useCase = new CourseQueryUseCaseImpl(stub);

        Course result = useCase.findById("course-1");

        assertThat(result).isEqualTo(course);
        assertThat(stub.findByIdArg).isEqualTo("course-1");
    }

    @DisplayName("Should return null when course not found by id")
    @Test
    void returnsNullWhenCourseNotFoundById() {
        var stub = new StubCourseRepository();
        stub.findByIdResult = null;
        var useCase = new CourseQueryUseCaseImpl(stub);

        assertThat(useCase.findById("missing")).isNull();
    }

    @DisplayName("Should return all courses for the given outlet id")
    @Test
    void returnsAllCoursesForOutletId() {
        Course c1 = Course.builder().id("c1").outletId("outlet-1").build();
        Course c2 = Course.builder().id("c2").outletId("outlet-1").build();
        var stub = new StubCourseRepository();
        stub.findAllByOutletIdResult = List.of(c1, c2);
        var useCase = new CourseQueryUseCaseImpl(stub);

        List<Course> result = useCase.findAllByOutletId("outlet-1");

        assertThat(result).containsExactly(c1, c2);
        assertThat(stub.findAllByOutletIdArg).isEqualTo("outlet-1");
    }

    @DisplayName("Should return an empty list when no courses are found for the outlet id")
    @Test
    void returnsEmptyListWhenNoCoursesForOutletId() {
        var stub = new StubCourseRepository();
        stub.findAllByOutletIdResult = List.of();
        var useCase = new CourseQueryUseCaseImpl(stub);

        assertThat(useCase.findAllByOutletId("outlet-empty")).isEmpty();
    }

    private static class StubCourseRepository implements CourseRepository {
        @Nullable Course findByIdResult;
        List<Course> findAllByOutletIdResult = List.of();
        String findByIdArg;
        String findAllByOutletIdArg;

        @Override
        public Course save(Course course) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @Nullable Course findById(String id) {
            this.findByIdArg = id;
            return findByIdResult;
        }

        @Override
        public List<Course> findAllByOutletId(String outletId) {
            this.findAllByOutletIdArg = outletId;
            return findAllByOutletIdResult;
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
