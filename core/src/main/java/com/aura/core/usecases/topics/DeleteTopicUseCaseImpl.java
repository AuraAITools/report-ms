package com.aura.core.usecases.topics;

import com.aura.core.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteTopicUseCaseImpl implements DeleteTopicUseCase {
    private final TopicRepository topicRepository;

    @Override
    public void deleteById(String id) {
        topicRepository.deleteById(id);
    }
}
