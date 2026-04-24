package com.aura.details.springdata.models.links;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.lessons.LessonPlanModel;
import com.aura.details.springdata.models.topics.TopicModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "LessonPlanTopicLinks", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_plan_topic",
                columnNames = {"lesson_plan_id", "topic_id"}
        )
})
public class LessonPlanTopicLinkModel extends TenantBaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_plan_id")
    private LessonPlanModel lessonPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private TopicModel topic;

}
