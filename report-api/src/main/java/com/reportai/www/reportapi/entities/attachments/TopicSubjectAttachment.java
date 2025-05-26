package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TopicSubjectAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_topic_subject",
                        columnNames = {"topic_id", "subject_id"}
                )
        })
public class TopicSubjectAttachment extends AttachmentTenantAwareBaseEntityTemplate<Topic, Subject> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Override
    public Topic getFirstEntity() {
        return topic;
    }

    @Override
    public Subject getSecondEntity() {
        return subject;
    }

    @Override
    public Collection<TopicSubjectAttachment> getFirstEntityAttachments() {
        return topic.getTopicSubjectAttachments() != null ? topic.getTopicSubjectAttachments() : null;
    }

    @Override
    public Collection<TopicSubjectAttachment> getSecondEntityAttachments() {
        return subject.getTopicSubjectAttachments() != null ? subject.getTopicSubjectAttachments() : null;
    }

    @Override
    public TopicSubjectAttachment create(Topic entity1, Subject entity2) {
        return TopicSubjectAttachment
                .builder()
                .topic(entity1)
                .subject(entity2)
                .build();
    }
}
