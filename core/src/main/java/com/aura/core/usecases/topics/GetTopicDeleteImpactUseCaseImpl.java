package com.aura.core.usecases.topics;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetTopicDeleteImpactUseCaseImpl implements GetTopicDeleteImpactUseCase {
    private final TopicRepository topicRepository;

    @Override
    public DeleteImpact getDeleteImpact(String topicId) {
        return topicRepository.findDeleteImpact(topicId);
    }
}
