package com.reportai.www.reportapi.services.topics;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.exceptions.lib.NotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicsService {

    private final InstitutionRepository institutionRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicsService(InstitutionRepository institutionRepository, TopicRepository topicRepository) {
        this.institutionRepository = institutionRepository;
        this.topicRepository = topicRepository;
    }

    // Topics
    public List<Topic> getAllTopicsFromInstitution(UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("no institution found"));
        return institution.getTopics();
    }

    @Transactional
    public Topic createTopicForInstitution(Topic topic, UUID institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new NotFoundException("institution does not exist"));
        topic.setInstitution(institution);
        return topicRepository.save(topic);
    }
}
