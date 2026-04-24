package com.aura.details.springdata.models.topics;


import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.LessonObjectiveTopicLinkModel;
import com.aura.details.springdata.models.links.LessonPlanTopicLinkModel;
import com.aura.details.springdata.models.links.MaterialTopicLinkModel;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Topics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tenant_topic_name",
                        columnNames = {"tenant_id", "name"}
                )
        })
public class TopicModel extends TenantBaseModel {

    private String name;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<MaterialTopicLinkModel> materialTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonObjectiveTopicLinkModel> lessonObjectiveTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TopicSubjectLinkModel> topicSubjectLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicLinkModel> lessonPlanTopicLinks = new HashSet<>();

}
