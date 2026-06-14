package com.aura.core.usecases.lessons;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.StudentCourseLink;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentLessonLinkRepository;
import com.aura.core.repositories.StudentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add students to lesson use case")
class AddStudentsToLessonUseCaseImplTest {

    @DisplayName("Should return empty list when studentIds is empty")
    @Test
    void returnsEmptyListWhenStudentIdsEmpty() {
        var stubLinkRepo = new StubStudentLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(null);
        var stubStudentRepo = new StubStudentRepository(List.of());
        var useCase = new AddStudentsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubStudentRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLesson("lesson-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create student-lesson links for each student ID")
    @Test
    void createsStudentLessonLinksForEachStudentId() {
        UUID lessonUuid = UUID.randomUUID();
        UUID studentUuid1 = UUID.randomUUID();
        UUID studentUuid2 = UUID.randomUUID();

        Lesson lesson = Lesson.builder().id(lessonUuid.toString()).name("Test Lesson").build();
        Student student1 = Student.builder().id(studentUuid1.toString()).build();
        Student student2 = Student.builder().id(studentUuid2.toString()).build();

        var stubLinkRepo = new StubStudentLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubStudentRepo = new StubStudentRepository(List.of(student1, student2));
        var useCase = new AddStudentsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubStudentRepo);

        List<StudentLessonLink> result = useCase.addStudentsToLesson(
                lessonUuid.toString(),
                List.of(studentUuid1.toString(), studentUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(lesson.getId(), link.getLessonId()));
        var studentIds = result.stream().map(StudentLessonLink::getStudentId).toList();
        assertTrue(studentIds.contains(studentUuid1.toString()));
        assertTrue(studentIds.contains(studentUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubStudentLessonLinkRepository implements StudentLessonLinkRepository {
        final List<List<StudentLessonLink>> savedAll = new ArrayList<>();

        @Override
        public StudentLessonLink save(StudentLessonLink studentLessonLink) {
            return studentLessonLink;
        }

        @Override
        public List<StudentLessonLink> saveAll(List<StudentLessonLink> studentLessonLinks) {
            savedAll.add(studentLessonLinks);
            return studentLessonLinks;
        }

        @Override
        public StudentLessonLink findById(java.util.UUID id) {
            return null;
        }

        @Override
        public List<StudentLessonLink> findAllByLessonIdAndStudentIds(String lessonId, List<String> studentIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<StudentLessonLink> assignLessonGroupToStudentLessonLinks(String lessonId, List<String> studentIds, String lessonGroupId) {
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
        public List<Lesson> findAllByOutletIdAndTimeRange(String tenantId,
                                                          String outletId,
                                                          java.time.Instant startInclusive,
                                                          java.time.Instant endExclusive) {
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

    private static class StubStudentRepository implements StudentRepository {
        private final List<Student> studentsToReturn;

        StubStudentRepository(List<Student> studentsToReturn) {
            this.studentsToReturn = studentsToReturn;
        }

        @Override
        public Student save(Student student) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Student findById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void updateProfileImageKeyById(String id, String profileImageKey) {
        }

        @Override
        public List<Student> findAllByIds(List<UUID> studentIds) {
            return studentsToReturn;
        }

        @Override
        public List<Student> saveAll(List<Student> students) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Level tagStudentToLevel(String studentId, String levelId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public School tagStudentToSchool(String studentId, String schoolId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Course createStudentCourseLink(String studentId, String courseId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<StudentCourseLink> createStudentCourseLinks(List<String> studentIds, String courseId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, String> findUserIdsByStudentIds(List<String> studentIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Student>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Student>> findAllByCourseIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllStudentCourseLinksByCourseId(String courseId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Student> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllStudentCourseLinksByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllStudentOutletLinksByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllStudentLessonLinksByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllSubjectStudentLinksByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllAccountStudentLinksByStudentId(String studentId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Student>> findAllByLessonGroupIds(List<String> lessonGroupIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, Level> findLevelsByStudentIds(List<String> studentIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, School> findSchoolsByStudentIds(List<String> studentIds) {
            throw new UnsupportedOperationException();
        }
    }
}
