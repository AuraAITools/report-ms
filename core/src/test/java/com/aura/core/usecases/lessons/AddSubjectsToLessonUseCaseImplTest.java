package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.links.SubjectLessonLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.SubjectLessonLinkRepository;
import com.aura.core.repositories.SubjectRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add subjects to lesson use case")
class AddSubjectsToLessonUseCaseImplTest {

    @DisplayName("Should return empty list when subjectIds is empty")
    @Test
    void returnsEmptyListWhenSubjectIdsEmpty() {
        var stubLinkRepo = new StubSubjectLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(null);
        var stubSubjectRepo = new StubSubjectRepository(List.of());
        var useCase = new AddSubjectsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubSubjectRepo);

        List<SubjectLessonLink> result = useCase.addSubjectsToLesson("lesson-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create subject-lesson links for each subject ID")
    @Test
    void createsSubjectLessonLinksForEachSubjectId() {
        UUID lessonUuid = UUID.randomUUID();
        UUID subjectUuid1 = UUID.randomUUID();
        UUID subjectUuid2 = UUID.randomUUID();

        Lesson lesson = Lesson.builder().id(lessonUuid.toString()).name("Test Lesson").build();
        Subject subject1 = Subject.builder().id(subjectUuid1.toString()).name("Subject 1").build();
        Subject subject2 = Subject.builder().id(subjectUuid2.toString()).name("Subject 2").build();

        var stubLinkRepo = new StubSubjectLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubSubjectRepo = new StubSubjectRepository(List.of(subject1, subject2));
        var useCase = new AddSubjectsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubSubjectRepo);

        List<SubjectLessonLink> result = useCase.addSubjectsToLesson(
                lessonUuid.toString(),
                List.of(subjectUuid1.toString(), subjectUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(lesson.getId(), link.getLessonId()));
        var subjectIds = result.stream().map(SubjectLessonLink::getSubjectId).toList();
        assertTrue(subjectIds.contains(subjectUuid1.toString()));
        assertTrue(subjectIds.contains(subjectUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubSubjectLessonLinkRepository implements SubjectLessonLinkRepository {
        final List<List<SubjectLessonLink>> savedAll = new ArrayList<>();

        @Override
        public SubjectLessonLink save(SubjectLessonLink subjectLessonLink) {
            return subjectLessonLink;
        }

        @Override
        public List<SubjectLessonLink> saveAll(List<SubjectLessonLink> subjectLessonLinks) {
            savedAll.add(subjectLessonLinks);
            return subjectLessonLinks;
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
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonLinksByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubSubjectRepository implements SubjectRepository {
        private final List<Subject> subjectsToReturn;

        StubSubjectRepository(List<Subject> subjectsToReturn) {
            this.subjectsToReturn = subjectsToReturn;
        }

        @Override
        public Subject save(Subject subject) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Subject> findAllByIds(List<UUID> subjectIds) {
            return subjectsToReturn;
        }

        @Override
        public SubjectWithTopics link(UUID topicId, List<UUID> ids, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<SubjectCourseLink> createSubjectCourseLinks(List<String> subjectIds, String courseId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Subject>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Subject findById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Subject> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Subject>> findAllByCourseIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Subject>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<com.aura.core.entities.topics.Topic>> findTopicsBySubjectIds(List<String> subjectIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllTopicSubjectLinksBySubjectId(String subjectId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllSubjectCourseLinksBySubjectId(String subjectId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllSubjectCourseLinksByCourseId(String courseId) {
            throw new UnsupportedOperationException();
        }
    }
}
