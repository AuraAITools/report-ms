package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.TopicLessonLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.TopicLessonLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add topics to lesson use case")
class AddTopicsToLessonUseCaseImplTest {

    @DisplayName("Should return empty list when topicIds is empty")
    @Test
    void returnsEmptyListWhenTopicIdsEmpty() {
        var stubLinkRepo = new StubTopicLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(null);
        var stubTopicRepo = new StubTopicRepository(List.of());
        var useCase = new AddTopicsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubTopicRepo);

        List<TopicLessonLink> result = useCase.addTopicsToLesson("lesson-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create topic-lesson links for each topic ID")
    @Test
    void createsTopicLessonLinksForEachTopicId() {
        UUID lessonUuid = UUID.randomUUID();
        UUID topicUuid1 = UUID.randomUUID();
        UUID topicUuid2 = UUID.randomUUID();

        Lesson lesson = Lesson.builder().id(lessonUuid.toString()).name("Test Lesson").build();
        Topic topic1 = Topic.builder().id(topicUuid1.toString()).name("Topic 1").build();
        Topic topic2 = Topic.builder().id(topicUuid2.toString()).name("Topic 2").build();

        var stubLinkRepo = new StubTopicLessonLinkRepository();
        var stubLessonRepo = new StubLessonRepository(lesson);
        var stubTopicRepo = new StubTopicRepository(List.of(topic1, topic2));
        var useCase = new AddTopicsToLessonUseCaseImpl(stubLinkRepo, stubLessonRepo, stubTopicRepo);

        List<TopicLessonLink> result = useCase.addTopicsToLesson(
                lessonUuid.toString(),
                List.of(topicUuid1.toString(), topicUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(lesson.getId(), link.getLessonId()));
        var topicIds = result.stream().map(TopicLessonLink::getTopicId).toList();
        assertTrue(topicIds.contains(topicUuid1.toString()));
        assertTrue(topicIds.contains(topicUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubTopicLessonLinkRepository implements TopicLessonLinkRepository {
        final List<List<TopicLessonLink>> savedAll = new ArrayList<>();

        @Override
        public TopicLessonLink save(TopicLessonLink topicLessonLink) {
            return topicLessonLink;
        }

        @Override
        public List<TopicLessonLink> saveAll(List<TopicLessonLink> topicLessonLinks) {
            savedAll.add(topicLessonLinks);
            return topicLessonLinks;
        }

        @Override
        public java.util.Set<String> findTopicIdsByLessonId(String lessonId) {
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
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonLinksByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubTopicRepository implements TopicRepository {
        private final List<Topic> topicsToReturn;

        StubTopicRepository(List<Topic> topicsToReturn) {
            this.topicsToReturn = topicsToReturn;
        }

        @Override
        public Topic save(Topic topic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Topic> findAllByIds(List<UUID> topicIds) {
            return topicsToReturn;
        }

        @Override
        public Topic findById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Topic> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<Topic>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllTopicSubjectLinksByTopicId(String topicId) {
            throw new UnsupportedOperationException();
        }
    }
}
