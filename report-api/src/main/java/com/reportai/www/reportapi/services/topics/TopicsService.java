package com.reportai.www.reportapi.services.topics;

import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicsService implements BaseServiceTemplate<Topic, UUID> {

    private final TopicRepository topicRepository;

    @Autowired
    public TopicsService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public JpaRepository<Topic, UUID> getRepository() {
        return this.topicRepository;
    }

    // Topics
    public Collection<Topic> getAllTopicsFromInstitution() {
        return topicRepository.findAll();
    }

    @Transactional
    public Topic createTopicForInstitution(Topic topic) {
        return topicRepository.save(topic);
    }
}
