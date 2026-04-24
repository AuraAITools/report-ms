package com.aura.details.springdata.models.topics;

import com.aura.details.springdata.models.base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "TopicSubjectLinkTemplates", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_topic_subject_name",
                columnNames = {"subject_name", "topic_name"}
        )
})
public class TopicSubjectLinkTemplateModel extends BaseModel {

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private String subjectName;
}
