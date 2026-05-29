package com.aura.core.usecases.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Topic query use case")
class TopicQueryUseCaseImplTest {

    @DisplayName("Should return topic found by id")
    @Test
    void findById_returnsTopic() {
        Topic topic = Topic.builder().id("t1").name("Algebra").build();
        var stub = new StubTopicRepository();
        stub.findByIdResult = topic;
        assertThat(new TopicQueryUseCaseImpl(stub).findById("t1")).isEqualTo(topic);
        assertThat(stub.findByIdArg).isEqualTo("t1");
    }

    @DisplayName("Should return null when topic not found by id")
    @Test
    void findById_returnsNullWhenMissing() {
        var stub = new StubTopicRepository();
        stub.findByIdResult = null;
        assertThat(new TopicQueryUseCaseImpl(stub).findById("missing")).isNull();
    }

    @DisplayName("Should return all topics in tenant")
    @Test
    void findAllByTenantId_returnsTopics() {
        Topic t1 = Topic.builder().id("t1").build();
        var stub = new StubTopicRepository();
        stub.findAllByTenantIdResult = List.of(t1);
        assertThat(new TopicQueryUseCaseImpl(stub).findAllByTenantId("inst-1")).containsExactly(t1);
        assertThat(stub.findAllByTenantIdArg).isEqualTo("inst-1");
    }

    @DisplayName("Should return empty list when no topics in tenant")
    @Test
    void findAllByTenantId_returnsEmpty() {
        var stub = new StubTopicRepository();
        stub.findAllByTenantIdResult = List.of();
        assertThat(new TopicQueryUseCaseImpl(stub).findAllByTenantId("inst-1")).isEmpty();
    }

    private static class StubTopicRepository implements TopicRepository {
        @Nullable Topic findByIdResult;
        List<Topic> findAllByTenantIdResult = List.of();
        String findByIdArg;
        String findAllByTenantIdArg;

        @Override
        public Topic save(Topic topic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @Nullable Topic findById(String id) {
            this.findByIdArg = id;
            return findByIdResult;
        }

        @Override
        public List<Topic> findAllByIds(List<UUID> topicIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Topic> findAllByTenantId(String tenantId) {
            this.findAllByTenantIdArg = tenantId;
            return findAllByTenantIdResult;
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
