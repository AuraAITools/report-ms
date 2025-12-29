package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.links.LessonObjectiveTopicLinkTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "LessonObjectives")
public class LessonObjectiveTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonTable lesson;

    @OneToMany(mappedBy = "lessonObjective", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<LessonObjectiveTopicLinkTable> lessonObjectiveTopicLinks = new HashSet<>();


}
