package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.LessonObjective;
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
@Table(name = "LessonObjectiveTopicAttachments", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_objective_topic",
                columnNames = {"lesson_objective_id", "topic_id"}
        )
})
public class LessonObjectiveTopicAttachment extends AttachmentTenantAwareBaseEntityTemplate<LessonObjective, Topic> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_objective_id")
    private LessonObjective lessonObjective;

    @Override
    public LessonObjective getFirstEntity() {
        return lessonObjective;
    }

    @Override
    public Topic getSecondEntity() {
        return topic;
    }

    @Override
    public Collection<LessonObjectiveTopicAttachment> getFirstEntityAttachments() {
        return lessonObjective != null ? lessonObjective.getLessonObjectiveTopicAttachments() : null;
    }

    @Override
    public Collection<LessonObjectiveTopicAttachment> getSecondEntityAttachments() {
        return topic != null ? topic.getLessonObjectiveTopicAttachments() : null;
    }

    @Override
    public LessonObjectiveTopicAttachment create(LessonObjective entity1, Topic entity2) {
        return LessonObjectiveTopicAttachment
                .builder()
                .lessonObjective(entity1)
                .topic(entity2)
                .build();
    }
}
