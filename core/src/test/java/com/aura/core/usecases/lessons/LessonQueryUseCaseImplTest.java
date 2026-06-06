package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lesson query use case")
class LessonQueryUseCaseImplTest {

    @DisplayName("Should return lesson found by id")
    @Test
    void findById_returnsLesson() {
        UUID id = UUID.randomUUID();
        Lesson lesson = Lesson.builder().id(id.toString()).name("L1").build();
        var stub = new StubLessonRepository();
        stub.findByIdResult = lesson;
        var useCase = new LessonQueryUseCaseImpl(stub);

        assertThat(useCase.findById(id)).isEqualTo(lesson);
        assertThat(stub.findByIdArg).isEqualTo(id);
    }

    @DisplayName("Should return null when lesson not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        var stub = new StubLessonRepository();
        stub.findByIdResult = null;
        assertThat(new LessonQueryUseCaseImpl(stub).findById(UUID.randomUUID())).isNull();
    }

    @DisplayName("Should return all lessons for the given course id")
    @Test
    void findAllByCourseId_returnsLessons() {
        Lesson l1 = Lesson.builder().id("l1").courseId("c1").build();
        Lesson l2 = Lesson.builder().id("l2").courseId("c1").build();
        var stub = new StubLessonRepository();
        stub.findAllByCourseIdResult = List.of(l1, l2);

        assertThat(new LessonQueryUseCaseImpl(stub).findAllByCourseId("c1")).containsExactly(l1, l2);
        assertThat(stub.findAllByCourseIdArg).isEqualTo("c1");
    }

    @DisplayName("Should return empty list when no lessons in course")
    @Test
    void findAllByCourseId_returnsEmpty() {
        var stub = new StubLessonRepository();
        stub.findAllByCourseIdResult = List.of();
        assertThat(new LessonQueryUseCaseImpl(stub).findAllByCourseId("c1")).isEmpty();
    }

    @DisplayName("Should return all lessons for the given outlet id")
    @Test
    void findAllByOutletId_returnsLessons() {
        Lesson l1 = Lesson.builder().id("l1").outletId("o1").build();
        Lesson l2 = Lesson.builder().id("l2").outletId("o1").build();
        var stub = new StubLessonRepository();
        stub.findAllByOutletIdResult = List.of(l1, l2);

        assertThat(new LessonQueryUseCaseImpl(stub).findAllByOutletId("o1")).containsExactly(l1, l2);
        assertThat(stub.findAllByOutletIdArg).isEqualTo("o1");
    }

    @DisplayName("Should return empty list when no lessons in outlet")
    @Test
    void findAllByOutletId_returnsEmpty() {
        var stub = new StubLessonRepository();
        stub.findAllByOutletIdResult = List.of();
        assertThat(new LessonQueryUseCaseImpl(stub).findAllByOutletId("o1")).isEmpty();
    }

    @DisplayName("Should return all lessons taught by the given educator id")
    @Test
    void findAllByEducatorId_returnsLessons() {
        Lesson l1 = Lesson.builder().id("l1").build();
        Lesson l2 = Lesson.builder().id("l2").build();
        var stub = new StubLessonRepository();
        stub.findAllByEducatorIdResult = List.of(l1, l2);

        assertThat(new LessonQueryUseCaseImpl(stub).findAllByEducatorId("e1")).containsExactly(l1, l2);
        assertThat(stub.findAllByEducatorIdArg).isEqualTo("e1");
    }

    @DisplayName("Should return empty list when educator has no lessons")
    @Test
    void findAllByEducatorId_returnsEmpty() {
        var stub = new StubLessonRepository();
        stub.findAllByEducatorIdResult = List.of();
        assertThat(new LessonQueryUseCaseImpl(stub).findAllByEducatorId("e1")).isEmpty();
    }

    @DisplayName("Should return all lessons attended by the given student id")
    @Test
    void findAllByStudentId_returnsLessons() {
        Lesson l1 = Lesson.builder().id("l1").build();
        Lesson l2 = Lesson.builder().id("l2").build();
        var stub = new StubLessonRepository();
        stub.findAllByStudentIdResult = List.of(l1, l2);

        assertThat(new LessonQueryUseCaseImpl(stub).findAllByStudentId("s1")).containsExactly(l1, l2);
        assertThat(stub.findAllByStudentIdArg).isEqualTo("s1");
    }

    @DisplayName("Should return empty list when student has no lessons")
    @Test
    void findAllByStudentId_returnsEmpty() {
        var stub = new StubLessonRepository();
        stub.findAllByStudentIdResult = List.of();
        assertThat(new LessonQueryUseCaseImpl(stub).findAllByStudentId("s1")).isEmpty();
    }

    private static class StubLessonRepository implements LessonRepository {
        @Nullable Lesson findByIdResult;
        List<Lesson> findAllByCourseIdResult = List.of();
        List<Lesson> findAllByOutletIdResult = List.of();
        List<Lesson> findAllByEducatorIdResult = List.of();
        List<Lesson> findAllByStudentIdResult = List.of();
        UUID findByIdArg;
        String findAllByCourseIdArg;
        String findAllByOutletIdArg;
        String findAllByEducatorIdArg;
        String findAllByStudentIdArg;

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
        public @Nullable Lesson findById(UUID id) {
            this.findByIdArg = id;
            return findByIdResult;
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
            this.findAllByCourseIdArg = courseId;
            return findAllByCourseIdResult;
        }

        @Override
        public List<Lesson> findAllByOutletId(String outletId) {
            this.findAllByOutletIdArg = outletId;
            return findAllByOutletIdResult;
        }

        @Override
        public List<Lesson> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Lesson> findAllByEducatorId(String educatorId) {
            this.findAllByEducatorIdArg = educatorId;
            return findAllByEducatorIdResult;
        }

        @Override
        public List<Lesson> findAllByStudentId(String studentId) {
            this.findAllByStudentIdArg = studentId;
            return findAllByStudentIdResult;
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
