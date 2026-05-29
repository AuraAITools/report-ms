package com.aura.core.usecases.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class TopicQueryUseCaseImpl implements TopicQueryUseCase {
    private final TopicRepository topicRepository;

    @Override
    public @Nullable Topic findById(String id) {
        return topicRepository.findById(id);
    }

    @Override
    public List<Topic> findAllByTenantId(String tenantId) {
        return topicRepository.findAllByTenantId(tenantId);
    }
}
