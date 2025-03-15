package com.reportai.www.reportapi.services.topics;

import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicsService implements BaseServiceTemplate<Topic, UUID> {

    private final InstitutionsService institutionsService;
    private final TopicRepository topicRepository;

    @Autowired
    public TopicsService(InstitutionsService institutionsService, TopicRepository topicRepository) {
        this.institutionsService = institutionsService;
        this.topicRepository = topicRepository;
    }

    @Override
    public JpaRepository<Topic, UUID> getRepository() {
        return this.topicRepository;
    }

    // Topics
    public List<Topic> getAllTopicsFromInstitution(UUID institutionId) {
        Institution institution = institutionsService.findById(institutionId);
        return institution.getTopics();
    }

    @Transactional
    public Topic createTopicForInstitution(Topic topic, UUID institutionId) {
        Institution institution = institutionsService.findById(institutionId);
        /**
         * TODO: refactor
         */
        topic.setInstitution(institution);
        return topicRepository.save(topic);
    }
}
