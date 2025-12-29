package com.aura.details.springdata.tables.links;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
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
@Table(name = "TopicSubjectLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_topic_subject",
                        columnNames = {"topic_id", "subject_id"}
                )
        })
public class TopicSubjectLinkTable extends TenantBaseTable {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "topic_id")
    private TopicTable topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private SubjectTable subject;

}
