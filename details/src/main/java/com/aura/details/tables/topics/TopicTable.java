package com.aura.details.tables.topics;


import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.links.LessonObjectiveTopicLinkTable;
import com.aura.details.tables.links.LessonPlanTopicLinkTable;
import com.aura.details.tables.links.MaterialTopicLinkTable;
import com.aura.details.tables.links.TopicSubjectLinkTable;
import jakarta.persistence.Entity;
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
import org.hibernate.envers.Audited;

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
public class TopicTable extends TenantBaseTable {

    private String name;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<MaterialTopicLinkTable> materialTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonObjectiveTopicLinkTable> lessonObjectiveTopicLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TopicSubjectLinkTable> topicSubjectLinks = new HashSet<>();

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicLinkTable> lessonPlanTopicLinks = new HashSet<>();

}
