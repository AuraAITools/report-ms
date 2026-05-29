package com.aura.core.usecases.topics;

import com.aura.core.entities.topics.Topic;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface TopicQueryUseCase {
    @Nullable Topic findById(String id);

    List<Topic> findAllByTenantId(String tenantId);
}
