package com.aura.details.services;

import com.aura.core.entities.topics.Topic;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicService {

    private final CreateTopicUseCase createTopicUseCase;

    public Topic createTopic(Topic topic) {
        return createTopicUseCase.createTopic(topic);
    }
}
