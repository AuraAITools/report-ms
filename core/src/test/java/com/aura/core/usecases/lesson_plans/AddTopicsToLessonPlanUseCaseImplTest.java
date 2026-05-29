package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.LessonPlanTopicLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add topics to lesson plan use case")
class AddTopicsToLessonPlanUseCaseImplTest {

    @DisplayName("Should return empty list when topicIds is empty")
    @Test
    void returnsEmptyListWhenTopicIdsEmpty() {
        var stubLinkRepo = new StubLessonPlanTopicLinkRepository();
        var stubLessonPlanRepo = new StubLessonPlanRepository(null);
        var stubTopicRepo = new StubTopicRepository(List.of());
        var useCase = new AddTopicsToLessonPlanUseCaseImpl(stubLinkRepo, stubLessonPlanRepo, stubTopicRepo);

        List<LessonPlanTopicLink> result = useCase.addTopicsToLessonPlan("plan-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create lesson-plan-topic links for each topic ID")
    @Test
    void createsLessonPlanTopicLinksForEachTopicId() {
        UUID planUuid = UUID.randomUUID();
        UUID topicUuid1 = UUID.randomUUID();
        UUID topicUuid2 = UUID.randomUUID();

        LessonPlan plan = LessonPlan.builder().id(planUuid.toString()).plan("My plan").build();
        Topic topic1 = Topic.builder().id(topicUuid1.toString()).name("Topic 1").build();
        Topic topic2 = Topic.builder().id(topicUuid2.toString()).name("Topic 2").build();

        var stubLinkRepo = new StubLessonPlanTopicLinkRepository();
        var stubLessonPlanRepo = new StubLessonPlanRepository(plan);
        var stubTopicRepo = new StubTopicRepository(List.of(topic1, topic2));
        var useCase = new AddTopicsToLessonPlanUseCaseImpl(stubLinkRepo, stubLessonPlanRepo, stubTopicRepo);

        List<LessonPlanTopicLink> result = useCase.addTopicsToLessonPlan(
                planUuid.toString(),
                List.of(topicUuid1.toString(), topicUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(plan.getId(), link.getLessonPlanId()));
        result.forEach(link -> assertEquals("tenant-1", link.getTenantId()));
        var topicIds = result.stream().map(LessonPlanTopicLink::getTopicId).toList();
        assertTrue(topicIds.contains(topicUuid1.toString()));
        assertTrue(topicIds.contains(topicUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubLessonPlanTopicLinkRepository implements LessonPlanTopicLinkRepository {
        final List<List<LessonPlanTopicLink>> savedAll = new ArrayList<>();

        @Override
        public List<LessonPlanTopicLink> saveAll(List<LessonPlanTopicLink> links) {
            savedAll.add(links);
            return links;
        }

        @Override
        public Map<String, List<Topic>> findTopicsByLessonPlanIds(List<String> planIds) {
            throw new UnsupportedOperationException();
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
