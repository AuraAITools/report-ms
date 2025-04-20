package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.lessons.LessonPlan;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
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

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LessonPlanTopicAttachments", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_plan_topic",
                columnNames = {"lesson_plan_id", "topic_id"}
        )
})
public class LessonPlanTopicAttachment extends AttachmentTenantAwareBaseEntityTemplate<LessonPlan, Topic, LessonPlanTopicAttachment> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_plan_id")
    private LessonPlan lessonPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Override
    public LessonPlan getFirstEntity() {
        return lessonPlan;
    }

    @Override
    public Topic getSecondEntity() {
        return topic;
    }

    @Override
    public Collection<LessonPlanTopicAttachment> getFirstEntityAttachments() {
        return lessonPlan.getLessonPlanTopicAttachments() != null ? lessonPlan.getLessonPlanTopicAttachments() : null;
    }

    @Override
    public Collection<LessonPlanTopicAttachment> getSecondEntityAttachments() {
        return topic.getLessonPlanTopicAttachments() != null ? topic.getLessonPlanTopicAttachments() : null;
    }

    @Override
    public LessonPlanTopicAttachment create(LessonPlan entity1, Topic entity2) {
        return LessonPlanTopicAttachment
                .builder()
                .lessonPlan(entity1)
                .topic(entity2)
                .build();
    }
}
