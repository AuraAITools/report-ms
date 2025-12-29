package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.links.LessonPlanMaterialsLinkTable;
import com.aura.details.springdata.tables.links.LessonPlanTopicLinkTable;
import com.aura.details.springdata.tables.links.StudentLessonLinkTable;
import jakarta.persistence.Column;
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
@Table(name = "LessonPlans")
public class LessonPlanTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonTable lesson;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isPlanned;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StudentLessonLinkTable> registeredStudents;

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanMaterialsLinkTable> lessonPlanMaterialsLinks = new HashSet<>();

    @OneToMany(mappedBy = "lessonPlan", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<LessonPlanTopicLinkTable> lessonPlanTopicLinks = new HashSet<>();

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        UNPLANNED
    }

}
