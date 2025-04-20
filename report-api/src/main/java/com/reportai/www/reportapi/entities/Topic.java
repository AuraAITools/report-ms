package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.LessonObjectiveTopicAttachment;
import com.reportai.www.reportapi.entities.attachments.LessonPlanTopicAttachment;
import com.reportai.www.reportapi.entities.attachments.MaterialTopicAttachment;
import com.reportai.www.reportapi.entities.attachments.TopicSubjectAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Topics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_topic_name",
                        columnNames = {"tenant_id", "name"}
                )
        })
public class Topic extends TenantAwareBaseEntity {

    private String name;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<MaterialTopicAttachment> materialTopicAttachments = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonObjectiveTopicAttachment> lessonObjectiveTopicAttachments = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TopicSubjectAttachment> topicSubjectAttachments = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicAttachment> lessonPlanTopicAttachments = new HashSet<>();

}
