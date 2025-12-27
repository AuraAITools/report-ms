package com.aura.details.tables.lessons;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.links.StudentLessonLinkTable;
import com.aura.details.tables.topics.TopicTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "LessonTopicProficiencies")
public class LessonTopicProficiencyTable extends TenantBaseTable {

    @Column
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TopicTable topic;

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentLessonLinkTable studentLessonLink;

}
