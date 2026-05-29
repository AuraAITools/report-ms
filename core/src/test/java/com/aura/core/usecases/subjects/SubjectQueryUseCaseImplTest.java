package com.aura.core.usecases.subjects;

import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.SubjectRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Subject query use case")
class SubjectQueryUseCaseImplTest {

    @DisplayName("Should return subject found by id")
    @Test
    void findById_returnsSubject() {
        Subject subject = Subject.builder().id("s1").name("Math").build();
        var stub = new StubSubjectRepository();
        stub.findByIdResult = subject;
        assertThat(new SubjectQueryUseCaseImpl(stub).findById("s1")).isEqualTo(subject);
        assertThat(stub.findByIdArg).isEqualTo("s1");
    }

    @DisplayName("Should return null when subject not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        var stub = new StubSubjectRepository();
        stub.findByIdResult = null;
        assertThat(new SubjectQueryUseCaseImpl(stub).findById("missing")).isNull();
    }

    @DisplayName("Should return all subjects in tenant")
    @Test
    void findAllByTenantId_returnsSubjects() {
        Subject s1 = Subject.builder().id("s1").build();
        Subject s2 = Subject.builder().id("s2").build();
        var stub = new StubSubjectRepository();
        stub.findAllByTenantIdResult = List.of(s1, s2);
        assertThat(new SubjectQueryUseCaseImpl(stub).findAllByTenantId("t1")).containsExactly(s1, s2);
        assertThat(stub.findAllByTenantIdArg).isEqualTo("t1");
    }

    @DisplayName("Should return empty list when no subjects in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        var stub = new StubSubjectRepository();
        stub.findAllByTenantIdResult = List.of();
        assertThat(new SubjectQueryUseCaseImpl(stub).findAllByTenantId("t1")).isEmpty();
    }

    @DisplayName("Should delegate findTopicsBySubjectIds to repository")
    @Test
    void findTopicsBySubjectIds_delegates() {
        Topic t1 = Topic.builder().id("t1").build();
        Map<String, List<Topic>> repoResult = Map.of("s1", List.of(t1));
        var stub = new StubSubjectRepository();
        stub.findTopicsBySubjectIdsResult = repoResult;
        assertThat(new SubjectQueryUseCaseImpl(stub).findTopicsBySubjectIds(List.of("s1"))).isEqualTo(repoResult);
    }

    private static class StubSubjectRepository implements SubjectRepository {
        @Nullable Subject findByIdResult;
        List<Subject> findAllByTenantIdResult = List.of();
        Map<String, List<Topic>> findTopicsBySubjectIdsResult = Map.of();
        String findByIdArg;
        String findAllByTenantIdArg;

        @Override
        public Subject save(Subject subject) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @Nullable Subject findById(String id) {
            this.findByIdArg = id;
            return findByIdResult;
        }

        @Override
        public List<Subject> findAllByIds(List<UUID> subjectIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Subject> findAllByTenantId(String tenantId) {
            this.findAllByTenantIdArg = tenantId;
            return findAllByTenantIdResult;
        }

        @Override
        public SubjectWithTopics link(UUID subjectId, List<UUID> topicIds, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<SubjectCourseLink> createSubjectCourseLinks(List<String> subjectIds, String courseId, String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Subject>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Subject>> findAllByCourseIds(List<String> courseIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Subject>> findAllByEducatorIds(List<String> educatorIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds) {
            return findTopicsBySubjectIdsResult;
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
