package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import com.aura.core.repositories.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Add topics to lesson group plan use case")
class AddTopicsToLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should return empty list when topicIds is empty")
    @Test
    void returnsEmptyListWhenTopicIdsEmpty() {
        var stubLinkRepo = new StubLessonGroupPlanTopicLinkRepository();
        var stubLessonGroupPlanRepo = new StubLessonGroupPlanRepository(null);
        var stubTopicRepo = new StubTopicRepository(List.of());
        var useCase = new AddTopicsToLessonGroupPlanUseCaseImpl(stubLinkRepo, stubLessonGroupPlanRepo, stubTopicRepo);

        List<LessonGroupPlanTopicLink> result = useCase.addTopicsToLessonGroupPlan("plan-id", List.of(), "tenant-1");

        assertTrue(result.isEmpty());
        assertTrue(stubLinkRepo.savedAll.isEmpty());
    }

    @DisplayName("Should create lesson-group-plan-topic links for each topic ID")
    @Test
    void createsLessonGroupPlanTopicLinksForEachTopicId() {
        UUID planUuid = UUID.randomUUID();
        UUID topicUuid1 = UUID.randomUUID();
        UUID topicUuid2 = UUID.randomUUID();

        LessonGroupPlan plan = LessonGroupPlan.builder().id(planUuid.toString()).plan("My plan").build();
        Topic topic1 = Topic.builder().id(topicUuid1.toString()).name("Topic 1").build();
        Topic topic2 = Topic.builder().id(topicUuid2.toString()).name("Topic 2").build();

        var stubLinkRepo = new StubLessonGroupPlanTopicLinkRepository();
        var stubLessonGroupPlanRepo = new StubLessonGroupPlanRepository(plan);
        var stubTopicRepo = new StubTopicRepository(List.of(topic1, topic2));
        var useCase = new AddTopicsToLessonGroupPlanUseCaseImpl(stubLinkRepo, stubLessonGroupPlanRepo, stubTopicRepo);

        List<LessonGroupPlanTopicLink> result = useCase.addTopicsToLessonGroupPlan(
                planUuid.toString(),
                List.of(topicUuid1.toString(), topicUuid2.toString()),
                "tenant-1");

        assertEquals(2, result.size());
        result.forEach(link -> assertEquals(plan.getId(), link.getLessonGroupPlanId()));
        result.forEach(link -> assertEquals("tenant-1", link.getTenantId()));
        var topicIds = result.stream().map(LessonGroupPlanTopicLink::getTopicId).toList();
        assertTrue(topicIds.contains(topicUuid1.toString()));
        assertTrue(topicIds.contains(topicUuid2.toString()));
    }

    // --- Stubs ---

    private static class StubLessonGroupPlanTopicLinkRepository implements LessonGroupPlanTopicLinkRepository {
        final List<List<LessonGroupPlanTopicLink>> savedAll = new ArrayList<>();

        @Override
        public List<LessonGroupPlanTopicLink> saveAll(List<LessonGroupPlanTopicLink> links) {
            savedAll.add(links);
            return links;
        }

        @Override
        public Map<String, List<Topic>> findTopicsByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Set<String> findTopicIdsByLessonGroupPlanId(String lessonGroupPlanId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllByLessonGroupPlanIdAndTopicIdIn(String lessonGroupPlanId, List<String> topicIds) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubLessonGroupPlanRepository implements LessonGroupPlanRepository {
        private final LessonGroupPlan planToReturn;

        StubLessonGroupPlanRepository(LessonGroupPlan planToReturn) {
            this.planToReturn = planToReturn;
        }

        @Override
        public LessonGroupPlan save(LessonGroupPlan plan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonGroupPlan findById(UUID id) {
            return planToReturn;
        }

        @Override
        public LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean existsByLessonGroupId(String lessonGroupId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonGroupPlan> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, LessonGroupPlan> findAllByLessonGroupIds(List<String> lessonGroupIds) {
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
        public Map<String, List<Topic>> findAllByLessonIds(List<String> lessonIds) {
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

        @Override
        public void deleteAllLessonObjectiveTopicLinksByTopicId(String topicId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllMaterialTopicLinksByTopicId(String topicId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllTopicLessonLinksByTopicId(String topicId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonGroupPlanTopicLinksByTopicId(String topicId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public com.aura.core.entities.delete_impact.DeleteImpact findDeleteImpact(String topicId) {
            throw new UnsupportedOperationException();
        }
    }
}
