package com.reportai.www.reportapi.services.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.attachments.TopicSubjectAttachment;
import com.reportai.www.reportapi.repositories.TopicSubjectAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleAttachmentService;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import com.reportai.www.reportapi.services.topics.TopicsService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * TopicSubjectAttachmentService creates and delete detachments for topics and subjects
 */
@Component
public class TopicSubjectAttachmentService implements ISimpleAttachmentService<Topic, Subject> {
    private final TopicsService topicsService;
    private final SubjectsService subjectsService;
    private final TopicSubjectAttachmentRepository topicSubjectAttachmentRepository;

    @Autowired
    public TopicSubjectAttachmentService(TopicsService topicsService, SubjectsService subjectsService, TopicSubjectAttachmentRepository topicSubjectAttachmentRepository) {
        this.topicsService = topicsService;
        this.subjectsService = subjectsService;
        this.topicSubjectAttachmentRepository = topicSubjectAttachmentRepository;
    }

    @Override
    public ISimpleRead<Topic> getEntity1ReadService() {
        return topicsService;
    }

    @Override
    public ISimpleRead<Subject> getEntity2ReadService() {
        return subjectsService;
    }

    @Override
    public JpaRepository<TopicSubjectAttachment, UUID> getAttachmentRepository() {
        return topicSubjectAttachmentRepository;
    }
}
