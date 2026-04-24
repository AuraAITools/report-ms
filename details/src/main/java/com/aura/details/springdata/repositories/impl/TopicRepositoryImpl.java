package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepository {

    private final JpaTopicRepository jpaTopicRepository;

    @Override
    public Topic save(Topic topic) {
        TopicModel newTopicModel = TopicMapper.INSTANCE.toModelEntity(topic);
        TopicModel createdTopicModel = jpaTopicRepository.save(newTopicModel);
        return TopicMapper.INSTANCE.toDomainEntity(createdTopicModel);
    }
}
