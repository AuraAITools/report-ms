package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.links.LessonGroupPlanTopicLink;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.LessonGroupPlanTopicLinkRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RemoveTopicsFromLessonGroupPlanUseCaseImpl")
class RemoveTopicsFromLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should delegate to repository with the plan id and topic ids")
    @Test
    void delegatesToRepository() {
        StubRepo repo = new StubRepo();
        var useCase = new RemoveTopicsFromLessonGroupPlanUseCaseImpl(repo);

        useCase.removeTopicsFromLessonGroupPlan("plan-1", List.of("t1", "t2"));

        assertEquals("plan-1", repo.capturedPlanId);
        assertEquals(List.of("t1", "t2"), repo.capturedTopicIds);
    }

    @DisplayName("Should be a no-op when topic ids is empty")
    @Test
    void noOpWhenEmpty() {
        StubRepo repo = new StubRepo();
        var useCase = new RemoveTopicsFromLessonGroupPlanUseCaseImpl(repo);

        useCase.removeTopicsFromLessonGroupPlan("plan-1", List.of());

        assertNull(repo.capturedPlanId);
        assertTrue(repo.capturedTopicIds.isEmpty());
    }

    private static class StubRepo implements LessonGroupPlanTopicLinkRepository {
        String capturedPlanId;
        List<String> capturedTopicIds = new ArrayList<>();

        @Override
        public List<LessonGroupPlanTopicLink> saveAll(List<LessonGroupPlanTopicLink> links) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Topic>> findTopicsByLessonGroupPlanIds(List<String> lessonGroupPlanIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<String> findTopicIdsByLessonGroupPlanId(String lessonGroupPlanId) {
            return new HashSet<>();
        }

        @Override
        public void deleteAllByLessonGroupPlanIdAndTopicIdIn(String lessonGroupPlanId, List<String> topicIds) {
            this.capturedPlanId = lessonGroupPlanId;
            this.capturedTopicIds = new ArrayList<>(topicIds);
        }
    }
}
