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

    private static class StubLessonRepository implements LessonRepository {
        @Nullable Lesson findByIdResult;
        List<Lesson> findAllByCourseIdResult = List.of();
        UUID findByIdArg;
        String findAllByCourseIdArg;

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
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonLinksByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }
    }
}
