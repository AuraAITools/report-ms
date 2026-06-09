package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.Lesson;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Lesson repository impl")
@ExtendWith(MockitoExtension.class)
class LessonRepositoryImplTest {

    @Mock
    private JpaLessonRepository jpaLessonRepository;
    @Mock
    private JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;
    @Mock
    private JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private LessonRepositoryImpl repository;

    @DisplayName("Should attach managed course and outlet proxies via EntityManager.getReference before persisting")
    @Test
    void saveAttachesCourseAndOutletRefs() {
        UUID courseId = UUID.randomUUID();
        UUID outletId = UUID.randomUUID();
        CourseModel courseProxy = CourseModel.builder().id(courseId).build();
        OutletModel outletProxy = OutletModel.builder().id(outletId).build();
        when(entityManager.getReference(CourseModel.class, courseId)).thenReturn(courseProxy);
        when(entityManager.getReference(OutletModel.class, outletId)).thenReturn(outletProxy);
        when(jpaLessonRepository.save(any(LessonModel.class))).thenReturn(new LessonModel());

        Lesson lesson = Lesson.builder()
                .courseId(courseId.toString())
                .outletId(outletId.toString())
                .name("Algebra Lesson")
                .build();
        repository.save(lesson);

        ArgumentCaptor<LessonModel> captor = ArgumentCaptor.forClass(LessonModel.class);
        verify(jpaLessonRepository).save(captor.capture());
        assertThat(captor.getValue().getCourse()).isSameAs(courseProxy);
        assertThat(captor.getValue().getOutlet()).isSameAs(outletProxy);
        assertThat(captor.getValue().getName()).isEqualTo("Algebra Lesson");
    }

    @DisplayName("Should group lessons by course id when fetching by course ids")
    @Test
    void findAllByCourseIds_groupsLessonsByCourse() {
        UUID courseId1 = UUID.randomUUID();
        UUID courseId2 = UUID.randomUUID();
        CourseModel c1 = CourseModel.builder().id(courseId1).build();
        CourseModel c2 = CourseModel.builder().id(courseId2).build();
        UUID lessonId1 = UUID.randomUUID();
        UUID lessonId2 = UUID.randomUUID();
        UUID lessonId3 = UUID.randomUUID();
        LessonModel l1 = LessonModel.builder().id(lessonId1).course(c1).name("L1").build();
        LessonModel l2 = LessonModel.builder().id(lessonId2).course(c1).name("L2").build();
        LessonModel l3 = LessonModel.builder().id(lessonId3).course(c2).name("L3").build();

        when(jpaLessonRepository.findByCourse_IdIn(List.of(courseId1, courseId2)))
                .thenReturn(List.of(l1, l2, l3));

        Map<String, List<Lesson>> result = repository
                .findAllByCourseIds(List.of(courseId1.toString(), courseId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(courseId1.toString())).extracting(Lesson::getName)
                .containsExactlyInAnyOrder("L1", "L2");
        assertThat(result.get(courseId2.toString())).extracting(Lesson::getName)
                .containsExactly("L3");
    }

    @DisplayName("Should return empty map when course id list is empty")
    @Test
    void findAllByCourseIds_emptyList_returnsEmptyMap() {
        assertThat(repository.findAllByCourseIds(List.of())).isEmpty();
    }

    @DisplayName("Should return all lessons whose course matches the given course id")
    @Test
    void findAllByCourseId_returnsLessons() {
        UUID courseId = UUID.randomUUID();
        CourseModel c = CourseModel.builder().id(courseId).build();
        LessonModel m1 = LessonModel.builder().id(UUID.randomUUID()).course(c).name("L1").build();
        LessonModel m2 = LessonModel.builder().id(UUID.randomUUID()).course(c).name("L2").build();
        when(jpaLessonRepository.findByCourse_IdIn(List.of(courseId))).thenReturn(List.of(m1, m2));

        List<Lesson> result = repository.findAllByCourseId(courseId.toString());

        assertThat(result).extracting(Lesson::getName).containsExactly("L1", "L2");
    }

    @DisplayName("Should return empty list when no lessons in course")
    @Test
    void findAllByCourseId_returnsEmpty() {
        UUID courseId = UUID.randomUUID();
        when(jpaLessonRepository.findByCourse_IdIn(List.of(courseId))).thenReturn(List.of());
        assertThat(repository.findAllByCourseId(courseId.toString())).isEmpty();
    }

    @DisplayName("Should return all lessons taught by the given educator id")
    @Test
    void findAllByEducatorId_returnsLessons() {
        UUID educatorId = UUID.randomUUID();
        LessonModel m1 = LessonModel.builder().id(UUID.randomUUID()).name("L1").build();
        LessonModel m2 = LessonModel.builder().id(UUID.randomUUID()).name("L2").build();
        when(jpaEducatorLessonLinkRepository.findLessonsByEducatorId(educatorId)).thenReturn(List.of(m1, m2));

        List<Lesson> result = repository.findAllByEducatorId(educatorId.toString());

        assertThat(result).extracting(Lesson::getName).containsExactly("L1", "L2");
    }

    @DisplayName("Should return empty list when educator has no lessons")
    @Test
    void findAllByEducatorId_returnsEmpty() {
        UUID educatorId = UUID.randomUUID();
        when(jpaEducatorLessonLinkRepository.findLessonsByEducatorId(educatorId)).thenReturn(List.of());
        assertThat(repository.findAllByEducatorId(educatorId.toString())).isEmpty();
    }

    @DisplayName("Should return all lessons attended by the given student id")
    @Test
    void findAllByStudentId_returnsLessons() {
        UUID studentId = UUID.randomUUID();
        LessonModel m1 = LessonModel.builder().id(UUID.randomUUID()).name("L1").build();
        LessonModel m2 = LessonModel.builder().id(UUID.randomUUID()).name("L2").build();
        when(jpaStudentLessonLinkRepository.findLessonsByStudentId(studentId)).thenReturn(List.of(m1, m2));

        List<Lesson> result = repository.findAllByStudentId(studentId.toString());

        assertThat(result).extracting(Lesson::getName).containsExactly("L1", "L2");
    }

    @DisplayName("Should return empty list when student has no lessons")
    @Test
    void findAllByStudentId_returnsEmpty() {
        UUID studentId = UUID.randomUUID();
        when(jpaStudentLessonLinkRepository.findLessonsByStudentId(studentId)).thenReturn(List.of());
        assertThat(repository.findAllByStudentId(studentId.toString())).isEmpty();
    }

    @Nested
    @DisplayName("findActiveOverlappingForEducator")
    class FindActiveOverlappingForEducator {

        @Test
        @DisplayName("Should return non-cancelled overlapping lessons for the educator, excluding the excluded lesson")
        void returnsOverlappingActive() {
            UUID educatorId = UUID.randomUUID();
            UUID excludeLessonId = UUID.randomUUID();
            Instant start = Instant.parse("2026-06-10T09:00:00Z");
            Instant end = Instant.parse("2026-06-10T10:00:00Z");

            LessonModel overlapping = LessonModel.builder()
                    .id(UUID.randomUUID())
                    .tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T08:30:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T09:30:00Z"))
                    .state(LessonModel.LESSON_STATE.CREATED)
                    .build();

            when(jpaEducatorLessonLinkRepository.findActiveOverlappingLessonsByEducator(
                    "tenant-1", educatorId, start, end, excludeLessonId))
                    .thenReturn(List.of(overlapping));

            List<Lesson> result = repository.findActiveOverlappingForEducator(
                    "tenant-1", educatorId.toString(), start, end, excludeLessonId.toString());

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(overlapping.getId().toString());
        }
    }

    @Nested
    @DisplayName("findActiveOverlappingForStudent")
    class FindActiveOverlappingForStudent {

        @Test
        @DisplayName("Should return non-cancelled overlapping lessons for the student, excluding the excluded lesson")
        void returnsOverlappingActive() {
            UUID studentId = UUID.randomUUID();
            UUID excludeLessonId = UUID.randomUUID();
            Instant start = Instant.parse("2026-06-10T09:00:00Z");
            Instant end = Instant.parse("2026-06-10T10:00:00Z");

            LessonModel overlapping = LessonModel.builder()
                    .id(UUID.randomUUID())
                    .tenantId("tenant-1")
                    .lessonStartTimestamptz(Instant.parse("2026-06-10T09:30:00Z"))
                    .lessonEndTimestamptz(Instant.parse("2026-06-10T10:30:00Z"))
                    .state(LessonModel.LESSON_STATE.CREATED)
                    .build();

            when(jpaStudentLessonLinkRepository.findActiveOverlappingLessonsByStudent(
                    "tenant-1", studentId, start, end, excludeLessonId))
                    .thenReturn(List.of(overlapping));

            List<Lesson> result = repository.findActiveOverlappingForStudent(
                    "tenant-1", studentId.toString(), start, end, excludeLessonId.toString());

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(overlapping.getId().toString());
        }
    }
}
