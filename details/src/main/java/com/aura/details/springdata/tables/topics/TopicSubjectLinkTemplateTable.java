package com.aura.details.springdata.tables.topics;

import com.aura.details.springdata.tables.base.BaseTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TopicSubjectLinkTemplates", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_topic_subject_name",
                columnNames = {"subject_name", "topic_name"}
        )
})
public class TopicSubjectLinkTemplateTable extends BaseTable {

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private String subjectName;
}
