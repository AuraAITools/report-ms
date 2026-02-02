package com.aura.core.usecases.topics;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTopicUseCaseImpl implements CreateTopicUseCase {

    private final TopicRepository topicRepository;

    @Override
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
