package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Add students to lesson plan use case")
class AddStudentsToLessonPlanUseCaseImplTest {

    @DisplayName("Should return empty list when studentIds is empty")
    @Test
    void emptyStudentIds_returnsEmpty() {
        var planRepo = new StubLessonPlanRepository(null);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(), List.of());
        var useCase = new AddStudentsToLessonPlanUseCaseImpl(planRepo, linkRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLessonPlan("plan-id", List.of(), "tenant-1");

        assertThat(result).isEmpty();
        assertThat(linkRepo.assignCalls).isEmpty();
    }

    @DisplayName("Should assign lesson plan to existing student-lesson links when all students are in the lesson")
    @Test
    void allStudentsInLesson_assignsPlan() {
        UUID planUuid = UUID.randomUUID();
        String lessonId = "lesson-001";
        LessonPlan plan = LessonPlan.builder().id(planUuid.toString()).lessonId(lessonId).build();
        StudentLessonLink l1 = StudentLessonLink.builder().studentId("stu-1").lessonId(lessonId).build();
        StudentLessonLink l2 = StudentLessonLink.builder().studentId("stu-2").lessonId(lessonId).build();

        var planRepo = new StubLessonPlanRepository(plan);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(l1, l2), List.of(l1, l2));
        var useCase = new AddStudentsToLessonPlanUseCaseImpl(planRepo, linkRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLessonPlan(
                planUuid.toString(), List.of("stu-1", "stu-2"), "tenant-1");

        assertThat(result).hasSize(2);
        assertThat(linkRepo.assignCalls).hasSize(1);
        assertThat(linkRepo.assignCalls.getFirst().lessonId()).isEqualTo(lessonId);
        assertThat(linkRepo.assignCalls.getFirst().lessonPlanId()).isEqualTo(plan.getId());
        assertThat(linkRepo.assignCalls.getFirst().studentIds()).containsExactlyInAnyOrder("stu-1", "stu-2");
    }

    @DisplayName("Should throw when any student is not in the lesson")
    @Test
    void studentNotInLesson_throws() {
        UUID planUuid = UUID.randomUUID();
        String lessonId = "lesson-001";
        LessonPlan plan = LessonPlan.builder().id(planUuid.toString()).lessonId(lessonId).build();
        StudentLessonLink l1 = StudentLessonLink.builder().studentId("stu-1").lessonId(lessonId).build();

        var planRepo = new StubLessonPlanRepository(plan);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(l1), List.of());
        var useCase = new AddStudentsToLessonPlanUseCaseImpl(planRepo, linkRepo);

        assertThatThrownBy(() -> useCase.addStudentsToLessonPlan(
                planUuid.toString(), List.of("stu-1", "stu-rogue"), "tenant-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("stu-rogue");
        assertThat(linkRepo.assignCalls).isEmpty();
    }

    // --- Stubs ---

    private record AssignCall(String lessonId, List<String> studentIds, String lessonPlanId) {
    }

    private static class StubStudentLessonLinkRepository implements StudentLessonLinkRepository {
        final java.util.List<AssignCall> assignCalls = new java.util.ArrayList<>();
        private final List<StudentLessonLink> existingByLesson;
        private final List<StudentLessonLink> afterAssign;

        StubStudentLessonLinkRepository(List<StudentLessonLink> existingByLesson,
                                        List<StudentLessonLink> afterAssign) {
            this.existingByLesson = existingByLesson;
            this.afterAssign = afterAssign;
        }

        @Override
        public StudentLessonLink save(StudentLessonLink studentLessonLink) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<StudentLessonLink> saveAll(List<StudentLessonLink> studentLessonLinks) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<StudentLessonLink> findAllByLessonIdAndStudentIds(String lessonId, List<String> studentIds) {
            return existingByLesson.stream().filter(l -> studentIds.contains(l.getStudentId())).toList();
        }

        @Override
        public List<StudentLessonLink> assignLessonPlanToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonPlanId) {
            assignCalls.add(new AssignCall(lessonId, studentIds, lessonPlanId));
            return afterAssign;
        }
    }

    private static class StubLessonPlanRepository implements LessonPlanRepository {
        private final LessonPlan planToReturn;

        StubLessonPlanRepository(LessonPlan planToReturn) {
            this.planToReturn = planToReturn;
        }

        @Override
        public LessonPlan save(LessonPlan lessonPlan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonPlan findById(UUID id) {
            return planToReturn;
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
}
