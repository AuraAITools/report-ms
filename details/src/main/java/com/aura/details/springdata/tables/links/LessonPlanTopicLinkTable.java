package com.aura.details.springdata.tables.links;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.lessons.LessonPlanTable;
import com.aura.details.springdata.tables.topics.TopicTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "LessonPlanTopicLinks", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_plan_topic",
                columnNames = {"lesson_plan_id", "topic_id"}
        )
})
public class LessonPlanTopicLinkTable extends TenantBaseTable {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_plan_id")
    private LessonPlanTable lessonPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private TopicTable topic;

}
