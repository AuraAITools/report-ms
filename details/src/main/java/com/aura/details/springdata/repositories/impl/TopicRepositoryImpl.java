package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.tables.topics.TopicTable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicRepositoryImpl implements TopicRepository {

    private final JpaTopicRepository jpaTopicRepository;

    @Override
    public Topic save(Topic topic) {
        TopicTable newTopicTable = TopicMapper.INSTANCE.toTableEntity(topic);
        TopicTable createdTopicTable = jpaTopicRepository.save(newTopicTable);
        return TopicMapper.INSTANCE.toDomainEntity(createdTopicTable);
    }
}
