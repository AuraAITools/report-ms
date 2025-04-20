package com.reportai.www.reportapi.services.topics;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.attachments.TopicSubjectAttachment;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.repositories.TopicSubjectAttachmentRepository;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicsService implements BaseServiceTemplate<Topic, UUID> {

    private final TopicRepository topicRepository;

    private final SubjectsService subjectsService;
    private final TopicSubjectAttachmentRepository topicSubjectAttachmentRepository;

    @Autowired
    public TopicsService(TopicRepository topicRepository, SubjectsService subjectsService,
                         TopicSubjectAttachmentRepository topicSubjectAttachmentRepository) {
        this.topicRepository = topicRepository;
        this.subjectsService = subjectsService;
        this.topicSubjectAttachmentRepository = topicSubjectAttachmentRepository;
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
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    @Transactional
    public Topic attach(UUID topicId, UUID subjectId) {
        Topic topic = findById(topicId);
        Subject subject = subjectsService.findById(subjectId);
        TopicSubjectAttachment topicSubjectAttachment = Attachment.createAndSync(topic, subject, new TopicSubjectAttachment());
        topicSubjectAttachmentRepository.save(topicSubjectAttachment);
        return topic;
    }
}
