package com.aura.core.usecases.topics;

import com.aura.core.entities.delete_impact.DeleteImpact;

public interface GetTopicDeleteImpactUseCase {
    DeleteImpact getDeleteImpact(String topicId);
}
