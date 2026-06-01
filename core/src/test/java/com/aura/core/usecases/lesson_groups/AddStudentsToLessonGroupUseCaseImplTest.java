package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Add students to lesson group use case")
class AddStudentsToLessonGroupUseCaseImplTest {

    @DisplayName("Should return empty list when studentIds is empty")
    @Test
    void emptyStudentIds_returnsEmpty() {
        var groupRepo = new StubLessonGroupRepository(null);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(), List.of());
        var useCase = new AddStudentsToLessonGroupUseCaseImpl(groupRepo, linkRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLessonGroup("group-id", List.of(), "tenant-1");

        assertThat(result).isEmpty();
        assertThat(linkRepo.assignCalls).isEmpty();
    }

    @DisplayName("Should assign lesson group to existing student-lesson links when all students are in the lesson")
    @Test
    void allStudentsInLesson_assignsGroup() {
        UUID groupUuid = UUID.randomUUID();
        String lessonId = "lesson-001";
        LessonGroup group = LessonGroup.builder().id(groupUuid.toString()).lessonId(lessonId).tenantId("tenant-1").build();
        StudentLessonLink l1 = StudentLessonLink.builder().studentId("stu-1").lessonId(lessonId).build();
        StudentLessonLink l2 = StudentLessonLink.builder().studentId("stu-2").lessonId(lessonId).build();

        var groupRepo = new StubLessonGroupRepository(group);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(l1, l2), List.of(l1, l2));
        var useCase = new AddStudentsToLessonGroupUseCaseImpl(groupRepo, linkRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLessonGroup(
                groupUuid.toString(), List.of("stu-1", "stu-2"), "tenant-1");

        assertThat(result).hasSize(2);
        assertThat(linkRepo.assignCalls).hasSize(1);
        assertThat(linkRepo.assignCalls.getFirst().lessonId()).isEqualTo(lessonId);
        assertThat(linkRepo.assignCalls.getFirst().lessonGroupId()).isEqualTo(group.getId());
        assertThat(linkRepo.assignCalls.getFirst().studentIds()).containsExactlyInAnyOrder("stu-1", "stu-2");
    }

    @DisplayName("Should throw when any student is not in the lesson")
    @Test
    void studentNotInLesson_throws() {
        UUID groupUuid = UUID.randomUUID();
        String lessonId = "lesson-001";
        LessonGroup group = LessonGroup.builder().id(groupUuid.toString()).lessonId(lessonId).tenantId("tenant-1").build();
        StudentLessonLink l1 = StudentLessonLink.builder().studentId("stu-1").lessonId(lessonId).build();

        var groupRepo = new StubLessonGroupRepository(group);
        var linkRepo = new StubStudentLessonLinkRepository(List.of(l1), List.of());
        var useCase = new AddStudentsToLessonGroupUseCaseImpl(groupRepo, linkRepo);

        assertThatThrownBy(() -> useCase.addStudentsToLessonGroup(
                groupUuid.toString(), List.of("stu-1", "stu-rogue"), "tenant-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("stu-rogue");
        assertThat(linkRepo.assignCalls).isEmpty();
    }

    // --- Stubs ---

    private record AssignCall(String lessonId, List<String> studentIds, String lessonGroupId) {
    }

    private static class StubStudentLessonLinkRepository implements StudentLessonLinkRepository {
        final List<AssignCall> assignCalls = new ArrayList<>();
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
        public List<StudentLessonLink> assignLessonGroupToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonGroupId) {
            assignCalls.add(new AssignCall(lessonId, studentIds, lessonGroupId));
            return afterAssign;
        }
    }

    private static class StubLessonGroupRepository implements LessonGroupRepository {
        private final LessonGroup groupToReturn;

        StubLessonGroupRepository(LessonGroup groupToReturn) {
            this.groupToReturn = groupToReturn;
        }

        @Override
        public LessonGroup save(LessonGroup lessonGroup) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonGroup findById(UUID id) {
            return groupToReturn;
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
